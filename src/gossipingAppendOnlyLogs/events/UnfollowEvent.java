package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnfollowEvent extends Event {

    public final PersonPublicKey unfollowedPerson;

    public UnfollowEvent(PersonPublicKey unfollowedPerson) {
        this.unfollowedPerson = unfollowedPerson;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(unfollowedPerson.hash());
    }
    
    @Override
    public void sign(PersonKeys keys, EventHash previousEventHash) {
        this.creatorId = keys.publicKey;
        this.previousEventHash = previousEventHash;
        this.signature = EventSignature.of(this, keys);
        
        this.uniqueId = RepastUtils.getNewUnfollowEventId();
    }
}
