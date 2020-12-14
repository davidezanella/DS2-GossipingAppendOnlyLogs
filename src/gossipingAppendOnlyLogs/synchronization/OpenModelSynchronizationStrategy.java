package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public class OpenModelSynchronizationStrategy extends SynchronizationStrategy {

	public OpenModelSynchronizationStrategy(Person local) {
		super(local);
	}

	public void synchronize(Store remoteStore, PersonPublicKey remoteId) {
		createUnknownLogs(remoteStore.getIds());
		updateKnownIdsWithRemoteStore(remoteStore);
	}
}
