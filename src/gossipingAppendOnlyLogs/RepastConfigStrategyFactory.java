package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.eventGeneration.*;
import gossipingAppendOnlyLogs.motion.*;
import gossipingAppendOnlyLogs.synchronization.*;
import repast.simphony.engine.environment.RunEnvironment;

public class RepastConfigStrategyFactory implements StrategyFactory {

	private static final String OPEN_MODEL = "OpenModel";
	private static final String TRANSITIVE_INTEREST = "TransitiveInterest";

	@Override
	public SynchronizationStrategy getCorrectStrategy(Person person) {
		String strategy = getSynchronizationProtocol();
		if (strategy.equals(OPEN_MODEL)) {
			return new OpenModelSynchronizationStrategy(person);
		} else {
			return new TransitiveInterestSynchronizationStrategy(person);
		}
	}

	@Override
	public MotionStrategy getMotionStrategy(Person person) {
		return new HabitMotionStrategy(person);
		//return new RandomMotionStrategy(person);
	}

	@Override
	public EventGenerationStrategy getEventGenerationStrategy(Person person) {
		String strategy = getSynchronizationProtocol();
		if (strategy.equals(OPEN_MODEL)) {
			return new SimpleEventGenerationStrategy(person);
		} else {
			return new EventGenerationStrategyWithInterests(person);
		}
	}

	private static String getSynchronizationProtocol() {
		var params = RunEnvironment.getInstance().getParameters();
		return params.getString("synchronizationProtocol");
	}
}
