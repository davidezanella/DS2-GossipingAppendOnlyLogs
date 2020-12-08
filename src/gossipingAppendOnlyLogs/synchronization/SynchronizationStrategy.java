package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public interface SynchronizationStrategy {
    void synchronize(Store localStore, Store remoteStore, PersonPublicKey localId, PersonPublicKey remoteId);
}
