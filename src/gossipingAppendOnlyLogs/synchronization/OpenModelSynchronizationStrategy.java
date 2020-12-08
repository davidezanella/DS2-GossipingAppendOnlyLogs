package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.models.Store;

public class OpenModelSynchronizationStrategy implements SynchronizationStrategy {
    @Override
    public void synchronize(Store localStore, Store remoteStore) {
    	// add new log ids
    	var remoteIds = remoteStore.getIds();
    	remoteIds.removeAll(localStore.getIds());
    	
    	remoteIds.stream().map(id -> remoteStore.get(id)).forEach(localStore::add);
    	System.out.println("Added " + remoteIds.size() + " new logs");
    	
    	// we should now update the stores
    	var frontier = localStore.getFrontier(localStore.getIds());
    	var news = remoteStore.getEventsSince(frontier);
    	localStore.update(news);
    }
}
