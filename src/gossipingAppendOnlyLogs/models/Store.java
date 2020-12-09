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

	public Set<PersonPublicKey> getLogsFollowedBy(PersonPublicKey id) {
		var log = logs.get(id);
		var events = log.getEvents(0, log.getLast());
		
		var following = events.stream().filter(e -> (e instanceof FollowEvent)).map(e -> (FollowEvent)e);
		var unfollowed = events.stream().filter(e -> (e instanceof UnfollowEvent)).map(e -> (UnfollowEvent)e);
		
		// TODO: fix adding index into events?
		var peopleFollowing = following.filter(e -> {
			int followIdx = events.indexOf(e);
			var numUnfollow = unfollowed.filter(u -> u.unfollowedPerson.equals(e.followedPerson)).filter(u -> {
				int unfollowIdx = events.indexOf(e);
				// stopped following that person later than starting following it
				return unfollowIdx > followIdx;
			}).count();
			// I'm currently following it
			return numUnfollow == 0;
		}).map(e -> e.followedPerson)
				.collect(Collectors.toSet());

		return peopleFollowing;
	}

	public Set<PersonPublicKey> getLogsBlockedBy(PersonPublicKey id) {
		var log = logs.get(id);
		var events = log.getEvents(0, log.getLast());
		
		var blocked = events.stream().filter(e -> (e instanceof BlockEvent)).map(e -> (BlockEvent)e);
		var unblocked = events.stream().filter(e -> (e instanceof UnblockEvent)).map(e -> (UnblockEvent)e);
		
		// TODO: fix adding index into events?
		var peopleBlocked = blocked.filter(e -> {
			int blockedIdx = events.indexOf(e);
			var numUnblocked = unblocked.filter(u -> u.unblockedPerson.equals(e.blockedPerson)).filter(u -> {
				int unblockedIdx = events.indexOf(e);
				// unblocked that person later than starting blocking it
				return unblockedIdx > blockedIdx;
			}).count();
			// he's now blocked
			return numUnblocked == 0;
		}).map(e -> e.blockedPerson)
				.collect(Collectors.toSet());

		return peopleBlocked;
	}
}
