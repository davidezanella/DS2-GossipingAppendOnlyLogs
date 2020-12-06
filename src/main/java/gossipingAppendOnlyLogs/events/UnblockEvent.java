package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnblockEvent extends Event {
    public UnblockEvent(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        super(creatorId, previousEventHash, index);
    }
}
