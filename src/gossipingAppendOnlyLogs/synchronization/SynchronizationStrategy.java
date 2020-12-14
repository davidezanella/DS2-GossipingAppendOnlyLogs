package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.Log;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public abstract class SynchronizationStrategy {	

    protected final Person local;
    protected final Store localStore;

    public SynchronizationStrategy(Person local) {
        this.local = local;
        this.localStore = local.getStore();
    }
    
    public abstract void synchronize(Store remoteStore, PersonPublicKey remoteId);

}
