package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;


public abstract class Event {

    private PersonPublicKey creatorId;
    private EventHash previousEventHash;
    private EventSignature signature;


    public void sign(PersonKeys keys, EventHash previousEventHash) {
        this.creatorId = keys.publicKey;
        this.previousEventHash = previousEventHash;
        this.signature = EventSignature.of(this, keys);
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
}
