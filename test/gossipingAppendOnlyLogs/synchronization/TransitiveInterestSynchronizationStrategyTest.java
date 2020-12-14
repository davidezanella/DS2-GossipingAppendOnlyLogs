package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.StrategyFactory;
import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.eventGeneration.EventGenerationStrategy;
import gossipingAppendOnlyLogs.motion.MotionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gossipingAppendOnlyLogs.CryptographyUtils.generateKeys;
import static org.junit.jupiter.api.Assertions.*;

class TransitiveInterestSynchronizationStrategyTest {
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
		var aliceSynchronizationStrategy = new TransitiveInterestSynchronizationStrategy(alice);

		// when
		aliceSynchronizationStrategy.synchronize(bob.getStore(), bob.getPublicKey());

		// then
		assertTrue(alice.getStore().getIds().contains(bob.getPublicKey()));
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