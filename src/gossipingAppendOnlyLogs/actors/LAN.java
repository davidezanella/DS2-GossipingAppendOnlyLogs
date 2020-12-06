package gossipingAppendOnlyLogs.actors;

import java.util.Set;

public class LAN {
	public final String ID;
	public final double maximumDistance = 5;

	public LAN(String id) {
		ID = id;
	}

	public Set<Person> getConnectedPeople() {
		throw new RuntimeException("not implemented yet");
	}
}
