package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.eventGeneration.*;
import gossipingAppendOnlyLogs.motion.*;
import gossipingAppendOnlyLogs.synchronization.*;
import repast.simphony.engine.environment.RunEnvironment;

/**
 * TODO: Maybe we use the Abstract Factory Pattern, but let's wait see how this file will grow
 */
public class StrategyUtils {

	private static final String OPEN_MODEL = "OpenModel";
	private static final String TRANSITIVE_INTEREST = "TransitiveInterest";

	public static SynchronizationStrategy getCorrectStrategy(Person person) {
		String strategy = getStringName();
		if (strategy.equals(OPEN_MODEL)) {
			return new OpenModelSynchronizationStrategy(person);
		} else {
			return new TransitiveInterestSynchronizationStrategy(person);
		}
	}

	public static MotionStrategy getMotionStrategy(Person person) {
		return new HabitMotionStrategy(person);
		//return new RandomMotionStrategy(person);
	}

	public static EventGenerationStrategy getEventGenerationStrategy(Person person) {
		String strategy = getStringName();
		if (strategy.equals(OPEN_MODEL)) {
			return new SimpleEventGenerationStrategy(person);
		} else {
			return new EventGenerationStrategyWithInterests(person);
		}
	}

	private static String getStringName() {
		var params = RunEnvironment.getInstance().getParameters();
		return params.getString("synchronizationProtocol");
	}
}
