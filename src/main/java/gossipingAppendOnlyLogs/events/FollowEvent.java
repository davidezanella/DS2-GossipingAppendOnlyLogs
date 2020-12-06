package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class FollowEvent extends Event {
    public FollowEvent(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        super(creatorId, previousEventHash, index);
    }
}
