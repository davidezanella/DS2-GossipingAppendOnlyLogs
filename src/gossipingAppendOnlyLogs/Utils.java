package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.synchronization.OpenModelSynchronizationStrategy;
import gossipingAppendOnlyLogs.synchronization.SynchronizationStrategy;
import gossipingAppendOnlyLogs.synchronization.TransitiveInterestSynchronizationStrategy;
import repast.simphony.engine.environment.RunEnvironment;

public class Utils {
	public static SynchronizationStrategy getCorrectStategy() {
		var params = RunEnvironment.getInstance().getParameters();
		var strategy = params.getString("synchronizationProtocol");

		if (strategy.equals("OpenModel"))
			return new OpenModelSynchronizationStrategy();
		else
			return new TransitiveInterestSynchronizationStrategy();
	}
}
