package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.eventGeneration.EventGenerationStrategy;
import gossipingAppendOnlyLogs.eventGeneration.SimpleEventGenerationStrategy;
import gossipingAppendOnlyLogs.motion.MotionStrategy;
import gossipingAppendOnlyLogs.motion.RandomMotionStrategy;
import gossipingAppendOnlyLogs.synchronization.OpenModelSynchronizationStrategy;
import gossipingAppendOnlyLogs.synchronization.SynchronizationStrategy;
import gossipingAppendOnlyLogs.synchronization.TransitiveInterestSynchronizationStrategy;
import repast.simphony.engine.environment.RunEnvironment;

/**
 * TODO: Maybe we use the Abstract Factory Pattern, but let's wait see how this file will grow
 */
public class StrategyUtils {
	public static SynchronizationStrategy getCorrectStrategy(Person person) {
		var params = RunEnvironment.getInstance().getParameters();
		var strategy = params.getString("synchronizationProtocol");

		if (strategy.equals("OpenModel"))
			return new OpenModelSynchronizationStrategy(person);
		else
			return new TransitiveInterestSynchronizationStrategy(person);
	}

	public static MotionStrategy getMotionStrategy(Person person) {
		return new RandomMotionStrategy(person);
	}

	public static EventGenerationStrategy getEventGenerationStrategy(Person person) {
		return new SimpleEventGenerationStrategy(person);
	}
}
