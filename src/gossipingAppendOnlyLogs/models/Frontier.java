package gossipingAppendOnlyLogs.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Frontier {
	private Set<FrontierItem> frontierItemSet = new HashSet<FrontierItem>();

	public void addFrontierItem(FrontierItem item) {
		frontierItemSet.add(item);
	}

	public int getSize() {
		return frontierItemSet.size();
	}

	public FrontierItem getItem(int i) {
		return (FrontierItem) frontierItemSet.toArray()[i];
	}

	public Iterator<FrontierItem> getIterator() {
		return frontierItemSet.iterator();
	}
}
