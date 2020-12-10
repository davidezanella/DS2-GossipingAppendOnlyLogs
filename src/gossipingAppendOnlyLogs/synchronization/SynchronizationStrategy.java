package gossipingAppendOnlyLogs.synchronization;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.models.Store;

public abstract class SynchronizationStrategy {	

    protected final Person person;

    public SynchronizationStrategy(Person person) {
        this.person = person;
    }
    
    public abstract void synchronize(Store localStore, Store remoteStore, PersonPublicKey localId, PersonPublicKey remoteId);
}
