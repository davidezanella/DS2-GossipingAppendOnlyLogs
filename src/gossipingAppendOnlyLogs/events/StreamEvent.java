package gossipingAppendOnlyLogs.events;


import gossipingAppendOnlyLogs.HashUtils;
import gossipingAppendOnlyLogs.RepastUtils;

public class StreamEvent extends Event {
     public final String content;

    public StreamEvent(String content) {
        this.content = content;
    }

	@Override
	protected String generateUniqueId() {
		return RepastUtils.getNewEventId();
	}

	@Override
    protected EventContentHash hashContent() {
        return new EventContentHash(HashUtils.hash(content));
    }
}
