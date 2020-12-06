package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.Event;

import java.util.ArrayList;
import java.util.List;

public class Log {
    private final List<Event> events = new ArrayList<>();

    public final PersonPublicKey id;

    public Log(PersonPublicKey id) {
        this.id = id;
    }

    public int getLast() {
        return 0;
    }

    public List<Event> getEvents(int start, int end) {
        return new ArrayList<>();
    }

    public void update (List<Event> remoteEvents) {

    }

    public void update(List<Event> localEvents, PersonPrivateKey privateKey) {

    }
}
