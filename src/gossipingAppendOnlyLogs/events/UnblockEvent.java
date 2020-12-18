package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnblockEvent extends Event {
    public final PersonPublicKey unblockedPerson;

    public UnblockEvent(PersonPublicKey unblockedPerson) {
        this.unblockedPerson = unblockedPerson;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(unblockedPerson.hash());
    }
    
    @Override
    public void sign(PersonKeys keys, EventHash previousEventHash) {
        this.creatorId = keys.publicKey;
        this.previousEventHash = previousEventHash;
        this.signature = EventSignature.of(this, keys);
        
        this.uniqueId = RepastUtils.getNewUnblockEventId();
    }
}
