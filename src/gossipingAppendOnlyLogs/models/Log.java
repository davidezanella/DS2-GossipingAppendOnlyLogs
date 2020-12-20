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

	public int getHeight() {
		return events.size();
	}

	public int getLast() {
		return events.size() - 1;
	}

	public List<Event> getEvents(int start, int end) {
		if (events.size() == 0)
			return new ArrayList<Event>();

		var toReturn = new ArrayList<Event>();
		// useful to tolerate start or end equal to -1
		for (int i = 0; i <= end; i++) {
			if (i >= start)
				toReturn.add(events.get(i));
		}

		return toReturn;
	}

	public void update(List<Event> remoteEvents) {
		remoteEvents
				.stream()
				.filter(e -> e.getCreatorId().equals(id))
				.filter(e -> !containsEvent(e))
				.filter(this::mayBeNextEvent)
				.forEach(events::add);
	}

	private boolean containsEvent(Event a) {
		return events
				.stream()
				.anyMatch(b -> b.hash().equals(a.hash()));
	}

	private boolean mayBeNextEvent(Event e) {
		if (events.isEmpty()) {
			return true;
		}
		var lastEvent = events.get(events.size() - 1);
		return lastEvent.hash().equals(e.getPreviousEventHash());
	}

	public void appendEvent(PersonKeys keysOfLogOwner, Event event) {
		var previousHash = events.isEmpty() ? null : events.get(events.size() - 1).hash();
		event.sign(keysOfLogOwner, previousHash);
		events.add(event);
	}
}
