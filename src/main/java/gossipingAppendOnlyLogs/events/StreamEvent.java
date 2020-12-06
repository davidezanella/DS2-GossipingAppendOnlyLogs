package gossipingAppendOnlyLogs.events;


public class StreamEvent extends Event {
     public final String content;

    public StreamEvent(String content) {
        this.content = content;
    }
}
