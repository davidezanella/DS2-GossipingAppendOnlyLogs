package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

import java.security.PrivateKey;

public abstract class Event {

    public final PersonPublicKey creatorId;
    public final EventHash previousEventHash;
    public final int index; // TODO: this is redundant, should we store it anyway? for reference, Bitcoin doesn't store the block height inside the block header
    public EventSignature signature;

    public Event(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        this.creatorId = creatorId;
        this.previousEventHash = previousEventHash;
        this.index = index;
    }

    public void sign(PrivateKey eventCreatorKey) {
        throw new RuntimeException("not implemented yet");
    }

    protected EventContentHash hashContent(){
        throw new RuntimeException("not implemented yet");
    }

    public EventHash hash() {
        if (!isSigned()) {
            throw new IllegalStateException("you have to sign the event before computing the hash");
        }
        // somehow hash creatorId, previousEventHash, signature and hashContent()
        throw new RuntimeException("not implemented yet");
    }

    public boolean isSigned() {
        return signature != null;
    }
}
