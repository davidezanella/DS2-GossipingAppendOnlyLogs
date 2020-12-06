package gossipingAppendOnlyLogs.events;

public class EventHash {
    public static EventHash of(Event event) {
        if (!event.isSigned()) {
            throw new IllegalStateException("Event must be signed before computing the hash");
        }
        throw new RuntimeException("not implemented yet");
    }
}
