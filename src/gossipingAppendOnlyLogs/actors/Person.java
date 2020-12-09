package gossipingAppendOnlyLogs.actors;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.StrategyUtils;
import gossipingAppendOnlyLogs.eventGeneration.EventGenerationStrategy;
import gossipingAppendOnlyLogs.events.Event;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;
import gossipingAppendOnlyLogs.motion.MotionStrategy;
import gossipingAppendOnlyLogs.synchronization.SynchronizationStrategy;
import repast.simphony.engine.schedule.ScheduledMethod;

import java.util.Set;

import static java.util.Collections.emptySet;

public class Person {
    public final String id; // TODO: Do we need an ID? can we just use the public key, and make the toString show an hash?

    private final PersonKeys keys;

	private final Store store = new Store();

    private final SynchronizationStrategy synchronizationStrategy = StrategyUtils.getCorrectStrategy();
    private final MotionStrategy motionStrategy = StrategyUtils.getMotionStrategy(this);
    private final EventGenerationStrategy eventGenerationStrategy = StrategyUtils.getEventGenerationStrategy(this);

    public Person(String id, PersonKeys keys) {
        this.id = id;
        this.keys = keys;
        createPersonalLog();
    }

    private void createPersonalLog() {
		store.add(new Log(keys.publicKey));
	}

    public PersonPublicKey getPublicKey() {
        return keys.publicKey;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void onTick() {
        motionStrategy.onTick();
        synchronizeWithConnectedPeople();
    }

    private void synchronizeWithConnectedPeople() {
        getConnectedPeople()
                .stream()
                .filter(person -> person != this)
                .forEach(person -> synchronizationStrategy.synchronize(this.store, person.store, this.getPublicKey(), person.getPublicKey()));
    }

    private Set<Person> getConnectedPeople(){
        var lan = getConnectedLAN();
        return lan != null ? lan.getConnectedPeople() : emptySet();
    }

    public LAN getConnectedLAN() {
        return RepastUtils
                .getAllLANsInGrid(this)
                .stream()
                .map(LanWithDistance::new)
                .filter(LanWithDistance::canConnect)
                .min((a, b) -> ((int)(a.distance - b.distance)))
                .map(lanWithDistance -> lanWithDistance.lan)
                .orElse(null);
    }

    private class LanWithDistance {
        final LAN lan;
        final double distance;

        public LanWithDistance(LAN lan) {
            this.lan = lan;
            this.distance = RepastUtils.getDistance(Person.this, lan);
        }

        public boolean canConnect(){
            return distance < lan.maximumDistance;
        }
    }

    public void addEventToPersonalLog(Event event){
    	store.get(keys.publicKey).appendEvent(keys, event);
	}
    
    public Store getStore() {
    	return this.store;
    }
}
