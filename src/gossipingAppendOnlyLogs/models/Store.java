package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.*;

import java.util.*;
import java.util.stream.Collectors;

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
		return new HashSet<>(logs.keySet());
	}

	public Frontier getFrontier(Set<PersonPublicKey> ids) {
		Frontier frontier = new Frontier();
		for (var logKey : logs.keySet()) {
			frontier.addFrontierItem(new FrontierItem(logKey, logs.get(logKey).getLast()));
		}
		return frontier;
	}

	public List<Event> getEventsSince(Frontier frontier) {
		var events = new ArrayList<Event>();
		var it = frontier.getIterator();

		while (it.hasNext()) {
			var item = it.next();
			var log = logs.get(item.id);
			if(log != null) {
				var lastIndex = log.getLast();

				if (lastIndex > item.last)
					events.addAll(log.getEvents(item.last, lastIndex));
			}
		}

		return events;
	}

	public void update(List<Event> remoteEvents) {
		for (var e : remoteEvents) {
			// this if condition should be removed once Transient interest sync is fixed
			if (logs.containsKey(e.getCreatorId())) {
				// I'm interested only in log ids that I already know

				// TODO: maybe change
				var evList = new ArrayList<Event>();
				evList.add(e);
				logs.get(e.getCreatorId()).update(evList);
			}
		}
	}

	public Set<PersonPublicKey> getLogsFollowedBy(PersonPublicKey id) {
		var log = logs.get(id);
		var events = log.getEvents(0, log.getLast());
		var followed = new HashSet<PersonPublicKey>();
		for (var event : events) {
			if (event instanceof FollowEvent) {
				followed.add(((FollowEvent) event).followedPerson);
			} else if (event instanceof UnfollowEvent) {
				followed.remove(((UnfollowEvent) event).unfollowedPerson);
			}
		}
		return followed;
	}

	public Set<PersonPublicKey> getLogsBlockedBy(PersonPublicKey id) {
		var log = logs.get(id);
		var events = log.getEvents(0, log.getLast());
		var blocked = new HashSet<PersonPublicKey>();
		for (var event : events) {
			if (event instanceof BlockEvent) {
				blocked.add(((BlockEvent) event).blockedPerson);
			} else if (event instanceof UnblockEvent) {
				blocked.remove(((UnblockEvent) event).unblockedPerson);
			}
		}
		return blocked;
	}
}
