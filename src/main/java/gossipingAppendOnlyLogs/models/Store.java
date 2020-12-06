package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Store {

    private Map<PersonPublicKey, Log> logs = new HashMap<>();

    public void add(Log log) {
        throw new RuntimeException("not implemented yet");
    }

    public void remove(PersonPublicKey id) {
        throw new RuntimeException("not implemented yet");
    }

    public Log get(PersonPublicKey id) {
        throw new RuntimeException("not implemented yet");
    }

    public Set<PersonPublicKey> getIds() {
        throw new RuntimeException("not implemented yet");
    }

    public Set<Frontier> getFrontiers(Set<PersonPublicKey> ids) {
        throw new RuntimeException("not implemented yet");
    }

    public List<Event> getEventsSince(Frontier frontier) {
        throw new RuntimeException("not implemented yet");
    }

    public void update(List<Event> remoteEvents) {
        throw new RuntimeException("not implemented yet");
    }

    public List<PersonPublicKey> getLogsFollowedBy(PersonPublicKey id) {
        throw new RuntimeException("not implemented yet");
    }

    public List<PersonPublicKey> getLogsBlockedBy(PersonPublicKey id) {
        throw new RuntimeException("not implemented yet");
    }
}
