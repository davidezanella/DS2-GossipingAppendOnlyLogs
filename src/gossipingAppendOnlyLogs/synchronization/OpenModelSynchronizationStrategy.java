package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public class OpenModelSynchronizationStrategy extends SynchronizationStrategy {

	public OpenModelSynchronizationStrategy(Person local) {
		super(local);
	}

	public void synchronize(Person remotePerson) {
		var remoteStore = remotePerson.getStore();
		createUnknownLogs(remoteStore.getIds());
		updateKnownIdsWithRemoteStore(remoteStore);
	}
}
