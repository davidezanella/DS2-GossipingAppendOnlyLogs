package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class StreamEvent extends Event {
    public StreamEvent(PersonPublicKey creatorId, EventHash previousEventHash, int index) {
        super(creatorId, previousEventHash, index);
    }
}
