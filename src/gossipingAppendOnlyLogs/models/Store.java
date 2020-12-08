package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Store {

	private Map<PersonPublicKey, Log> logs = new HashMap<>();

	public void add(Log log) {
		if (logs.containsKey(log.id)) {
			throw new IllegalStateException("The log is already present in this store!");
		}
		logs.put(log.id, log);
	}

	public void remove(PersonPublicKey id) {
		logs.remove(id);
	}

	public Log get(PersonPublicKey id) {
		return logs.get(id);
	}

	public Set<PersonPublicKey> getIds() {
		return logs.keySet();
	}

	public Frontier getFrontier(Set<PersonPublicKey> ids) {
		Frontier frontier = new Frontier();
		for (var logKey : logs.keySet()) {
			frontier.addFrontierItem(new FrontierItem(logs.get(logKey).id, logs.get(logKey).getLast()));
		}
		return frontier;
	}

	public List<Event> getEventsSince(Frontier frontier) {
		var events = new ArrayList<Event>();
		var it = frontier.getIterator();

		while (it.hasNext()) {
			var item = it.next();
			var log = logs.get(item.id);
			var lastIndex = log.getLast();

			if (lastIndex > item.last)
				events.addAll(log.getEvents(item.last, lastIndex));
		}

		return events;
	}

	public void update(List<Event> remoteEvents) {
		for (var e : remoteEvents) {
			if (logs.containsKey(e.getCreatorId())) {
				// I'm interested only in log ids that I already know

				// TODO: maybe change
				var evList = new ArrayList<Event>();
				evList.add(e);
				logs.get(e.getCreatorId()).update(evList);
			}
		}
	}

	public List<PersonPublicKey> getLogsFollowedBy(PersonPublicKey id) {
		throw new RuntimeException("not implemented yet");
	}

	public List<PersonPublicKey> getLogsBlockedBy(PersonPublicKey id) {
		throw new RuntimeException("not implemented yet");
	}
}
