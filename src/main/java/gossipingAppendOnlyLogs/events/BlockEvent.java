package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class BlockEvent extends Event {
    public BlockEvent(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        super(creatorId, previousEventHash, index);
    }
}
