package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.models.Store;

public interface SynchronizationStrategy {
    void synchronize(Store localStore, Store remoteStore);
}
