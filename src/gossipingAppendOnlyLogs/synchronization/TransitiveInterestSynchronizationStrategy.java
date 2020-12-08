package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;
import gossipingAppendOnlyLogs.models.Log;

public class TransitiveInterestSynchronizationStrategy implements SynchronizationStrategy {
    @Override
    public void synchronize(Store localStore, Store remoteStore, PersonPublicKey localId, PersonPublicKey remoteId) {
    	// add missing following log ids
    	var followingIds = localStore.getLogsFollowedBy(localId);
    	followingIds.removeAll(localStore.getIds());

    	followingIds.stream().map(id -> new Log(id)).forEach(localStore::add);
    	
    	// remove blocked log ids
    	var blockedIds = localStore.getLogsBlockedBy(localId);
    	blockedIds.retainAll(localStore.getIds());
    	
    	blockedIds.stream().forEach(localStore::remove);

    	// we should now update the stores
    	var frontier = localStore.getFrontier(localStore.getIds());
    	var news = remoteStore.getEventsSince(frontier);
    	localStore.update(news);
    }
}
