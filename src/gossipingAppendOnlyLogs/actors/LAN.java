package gossipingAppendOnlyLogs.actors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import gossipingAppendOnlyLogs.RepastUtils;

public class LAN {
	public final String ID;
	public final double maximumDistance = 5;

	public LAN(String id) {
		ID = id;
	}

	public Set<Person> getConnectedPeople() {
		var people = RepastUtils.getAllPeopleInGrid(this).stream()
				.filter(p -> {
					var lan = p.getConnectedLAN();
					if (lan == null)
						return false;
					return lan.ID.equals(ID);
				})
				.toArray(Person[]::new);

		Set<Person> set = new HashSet<>(); 
		Collections.addAll(set, people); 
		return set;
	}
}
