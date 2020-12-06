package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.Event;
import gossipingAppendOnlyLogs.events.EventHash;
import gossipingAppendOnlyLogs.events.StreamEvent;

import java.util.ArrayList;
import java.util.List;

public class Log {
    private final List<Event> events = new ArrayList<>();

    public final PersonPublicKey id;

    public Log(PersonPublicKey id) {
        this.id = id;
    }

    public int getHeight() {
        return events.size();
    }

    public List<Event> getEvents(int start, int end) {
        throw new RuntimeException("not implemented yet");
    }

    public void update (List<Event> remoteEvents) {
        throw new RuntimeException("not implemented yet");
    }

    public void update(List<Event> localEvents, PersonPrivateKey privateKey) {
        throw new RuntimeException("not implemented yet");
    }

    public void addContent(PersonKeys keysOfLogOwner, String content) {
        appendEvent(keysOfLogOwner, new StreamEvent(content));
    }

    protected void appendEvent(PersonKeys keysOfLogOwner, Event event) {
        var previousHash = events.isEmpty() ? null : events.get(events.size() - 1).hash();
        event.sign(keysOfLogOwner, previousHash);
        events.add(event);
    }
}
