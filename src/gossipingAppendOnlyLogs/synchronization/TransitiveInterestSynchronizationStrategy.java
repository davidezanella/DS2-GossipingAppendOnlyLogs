package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public class TransitiveInterestSynchronizationStrategy extends SynchronizationStrategy {

    public TransitiveInterestSynchronizationStrategy(Person person) {
		super(person);
	}

	public void synchronize(Store remoteStore, PersonPublicKey remoteId) {
		createUnknownLogs(remoteStore);

		// we should now update the stores
		var frontier = localStore.getFrontier(localStore.getIds());
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
