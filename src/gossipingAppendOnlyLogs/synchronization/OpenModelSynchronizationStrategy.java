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
		createUnknownLogs(remoteStore);

		// we should now update the local store with the new events that the remote store knows
		var frontier = localStore.getFrontier(remoteStore.getIds());
		var news = remoteStore.getEventsSince(frontier);
		localStore.update(news);
		
		// save new events to a list for logging purposes
		local.addedEvents.addAll(news);
	}

	protected void createUnknownLogs(Store remoteStore) {
		var knownIds = localStore.getIds();
		remoteStore.getIds()
				.stream()
				.filter(id -> !knownIds.contains(id))
				.map(Log::new)
				.forEach(localStore::add);
		System.out.println("Added " + (localStore.getIds().size() - knownIds.size()) + " new logs");
	}
}
