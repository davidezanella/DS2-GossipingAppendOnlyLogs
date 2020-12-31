package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;


public abstract class Event {

    protected PersonPublicKey creatorId;
    protected EventHash previousEventHash;
    protected EventSignature signature;
    
    protected final String uniqueId = RepastUtils.getNewEventId(); // used for logging purposes only

	protected EventHash hashAfterSignature;

	public final void sign(PersonKeys keys, EventHash previousEventHash) {
		if (hashAfterSignature != null) {
			throw new IllegalStateException("event has already been signed");
		}
		this.creatorId = keys.publicKey;
		this.previousEventHash = previousEventHash;
		this.signature = EventSignature.of(this, keys);
		hashAfterSignature = EventHash.of(this);
	}

    protected abstract EventContentHash hashContent();

    public EventHash hash() {
		if (hashAfterSignature == null) {
			throw new IllegalStateException("event has not be signed yet");
		}
		return hashAfterSignature;
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
