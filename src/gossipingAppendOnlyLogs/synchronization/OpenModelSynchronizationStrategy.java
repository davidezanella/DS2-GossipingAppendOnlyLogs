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
		// add new log ids
		createMissingLogs(localStore, remoteStore);
		createMissingLogs(remoteStore, localStore);

		// we should now update the stores
		var frontier = localStore.getFrontier(localStore.getIds());
		var news = remoteStore.getEventsSince(frontier);
		localStore.update(news);
		
		// save new events to a list for logging purposes
		person.addedEvents.addAll(news);
	}

	private void createMissingLogs(Store source, Store dest) {
		var remoteIds = dest.getIds();
		remoteIds.removeAll(source.getIds());

		remoteIds.stream().map(id -> new Log(id)).forEach(source::add);
		System.out.println("Added " + remoteIds.size() + " new logs");
	}
}
