package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnfollowEvent extends Event {
    public UnfollowEvent(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        super(creatorId, previousEventHash, index);
    }
}
