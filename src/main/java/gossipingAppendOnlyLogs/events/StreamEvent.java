package gossipingAppendOnlyLogs.events;


import gossipingAppendOnlyLogs.HashUtils;

public class StreamEvent extends Event {
     public final String content;

    public StreamEvent(String content) {
        this.content = content;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(HashUtils.hash(content));
    }
}
