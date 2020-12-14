package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.StrategyFactory;
import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.eventGeneration.EventGenerationStrategy;
import gossipingAppendOnlyLogs.events.StreamEvent;
import gossipingAppendOnlyLogs.motion.MotionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gossipingAppendOnlyLogs.CryptographyUtils.generateKeys;
import static org.junit.jupiter.api.Assertions.*;

class OpenModelSynchronizationStrategyTest {

	private Person alice;
	private Person bob;

	@BeforeEach
	public void setup(){
		alice = createPerson("alice");
		bob = createPerson("bob");
	}

	@Test
	public void aliceDiscoverBobEmptyLog() {
		// given
		var aliceSynchronizationStrategy = new OpenModelSynchronizationStrategy(alice);

		// TODO: we actually do not need to pass alice's store and key, the strategy already known Alice
		// when
		aliceSynchronizationStrategy.synchronize(alice.getStore(), bob.getStore(), alice.getPublicKey(), bob.getPublicKey());

		// then
		assertTrue(alice.getStore().getIds().contains(bob.getPublicKey()));
	}

	@Test
	public void aliceDiscoverBobLogWithOneEvent() {
		// given
		bob.addEventToPersonalLog(new StreamEvent("My first post"));
		var aliceSynchronizationStrategy = new OpenModelSynchronizationStrategy(alice);

		// when
		aliceSynchronizationStrategy.synchronize(alice.getStore(), bob.getStore(), alice.getPublicKey(), bob.getPublicKey());

		// then
		assertEquals(1, alice.getStore().get(bob.getPublicKey()).getHeight());
	}

	private Person createPerson(String id) {
		return new Person(id, generateKeys(), new StrategyFactory() {
			@Override
			public SynchronizationStrategy getCorrectStrategy(Person person) {
				return null;
			}

			@Override
			public MotionStrategy getMotionStrategy(Person person) {
				return null;
			}

			@Override
			public EventGenerationStrategy getEventGenerationStrategy(Person person) {
				return null;
			}
		});
	}
}
