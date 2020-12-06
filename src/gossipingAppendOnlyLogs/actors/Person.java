package gossipingAppendOnlyLogs.actors;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;
import gossipingAppendOnlyLogs.synchronization.OpenModelSynchronizationStrategy;
import gossipingAppendOnlyLogs.synchronization.SynchronizationStrategy;
import repast.simphony.engine.schedule.ScheduledMethod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public class Person {
    public final String id; // TODO: Do we need an ID? can we just use the public key, and make the toString show an hash?

    private final PersonKeys keys;

    private final SynchronizationStrategy synchronizationStrategy = new OpenModelSynchronizationStrategy();

    private final Store store = new Store();

    public Person(String id, PersonKeys keys) {
        this.id = id;
        this.keys = keys;
    }

    public PersonPublicKey getPublicKey() {
        return keys.publicKey;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void onTick() {
        //moveOrStay();
        //synchronizeWithConnectedPeople();
    }

    private void moveOrStay() {
        throw new RuntimeException("not implemented yet");
    }

    private void synchronizeWithConnectedPeople() {
        getConnectedPeople()
                .stream()
                .filter(person -> person != this)
                .forEach(person -> synchronizationStrategy.synchronize(this.store, person.store));
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

}
