package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;


public abstract class Event {

    protected PersonPublicKey creatorId;
    protected EventHash previousEventHash;
    protected EventSignature signature;
    
    protected String uniqueId; // used for logging purposes only


    public void sign(PersonKeys keys, EventHash previousEventHash) {
        this.creatorId = keys.publicKey;
        this.previousEventHash = previousEventHash;
        this.signature = EventSignature.of(this, keys);
        
        this.uniqueId = RepastUtils.getNewEventId();
    }

    protected abstract EventContentHash hashContent();

    public EventHash hash() {
        return EventHash.of(this);
    }

    public boolean isSigned() {
        return signature != null;
    }

    public EventSignature getSignature() {
        return signature;
    }

    public PersonPublicKey getCreatorId() {
        return creatorId;
    }

    public EventHash getPreviousEventHash() {
        return previousEventHash;
    }
    
    @Override
    public String toString() {
    	return uniqueId;
    }
}
