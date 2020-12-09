package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public class TransitiveInterestSynchronizationStrategy implements SynchronizationStrategy {
    @Override
    public void synchronize(Store localStore, Store remoteStore, PersonPublicKey localId, PersonPublicKey remoteId) {
    	createMissingLogs(localStore, localId);
    	createMissingLogs(remoteStore, remoteId);

    	// we should now update the stores
    	var frontier = localStore.getFrontier(localStore.getIds());
    	var news = remoteStore.getEventsSince(frontier);
    	localStore.update(news);
    }

	private void createMissingLogs(Store store, PersonPublicKey creatorId) {
		// add missing following log ids
    	var followingIds = store.getLogsFollowedBy(creatorId);
    	followingIds.removeAll(store.getIds());

    	followingIds.stream().map(id -> new Log(id)).forEach(store::add);
    	
    	// remove blocked log ids
    	var blockedIds = store.getLogsBlockedBy(creatorId);
    	blockedIds.retainAll(store.getIds());
    	
    	blockedIds.stream().forEach(store::remove);
	}
}
