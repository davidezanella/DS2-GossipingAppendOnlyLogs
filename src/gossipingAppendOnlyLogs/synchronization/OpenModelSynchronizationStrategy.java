package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public class OpenModelSynchronizationStrategy extends SynchronizationStrategy {
	public OpenModelSynchronizationStrategy(Person person) {
		super(person);
	}

	public void synchronize(Store localStore, Store remoteStore, PersonPublicKey localId, PersonPublicKey remoteId) {
		createUnknownLogs(localStore, remoteStore);

		// we should now update the local store with the new events that the remote store knows
		var frontier = localStore.getFrontier(remoteStore.getIds());
		var news = remoteStore.getEventsSince(frontier);
		localStore.update(news);
		
		// save new events to a list for logging purposes
		person.addedEvents.addAll(news);
	}

	private void createUnknownLogs(Store local, Store remote) {
		var knownIds = local.getIds();
		remote.getIds()
				.stream()
				.filter(id -> !knownIds.contains(id))
				.map(Log::new)
				.forEach(local::add);
		System.out.println("Added " + (local.getIds().size() - knownIds.size()) + " new logs");
	}
}
