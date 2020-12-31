package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.actors.LAN;
import gossipingAppendOnlyLogs.actors.Person;
import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepastUtils {

    public static Grid<Object> grid;

    public static Context<Object> context;

    public static ContinuousSpace<Object> space;
    
    public static int lastEventId = -1; // used for logging purposes
    public static int lastSpecialEventId = -1; // used for logging purposes, special events are follows/unfollows/...

	private static final List<Person> people = new ArrayList<>();
	private static final List<LAN> LANs = new ArrayList<>();

    public static List<LAN> getAllLANsInGrid(Object actor) {
        return LANs;
    }

    public static List<Person> getAllPeopleInGrid(Object actor) {
        return people;
    }

    public static double getDistance(Person person, LAN lan) {
        return space.getDistance(
                space.getLocation(person),
                space.getLocation(lan)
        );
    }

    public static void moveTo(Object obj, double x, double y) {
        space.moveTo(obj, x, y);
        grid.moveTo(obj, (int) x, (int) y);
    }

	public static NdPoint getRandomPoint() {
		return new NdPoint(
				RandomHelper.nextIntFromTo(0, (int) space.getDimensions().getWidth() - 1),
				RandomHelper.nextIntFromTo(0, (int) space.getDimensions().getHeight() - 1)
		);
	}
    
    public static String getNewEventId() {
    	lastEventId++;
    	return "Event" + lastEventId;
    }
    
    public static String getNewFollowEventId() {
    	lastSpecialEventId++;
    	return "FollowEvent" + lastSpecialEventId;
    }
    
    public static String getNewUnfollowEventId() {
    	lastSpecialEventId++;
    	return "UnfollowEvent" + lastSpecialEventId;
    }
    
    public static String getNewBlockEventId() {
    	lastSpecialEventId++;
    	return "BlockEvent" + lastSpecialEventId;
    }
    
    public static String getNewUnblockEventId() {
    	lastSpecialEventId++;
    	return "UnblockEvent" + lastSpecialEventId;
    }

	public static void addLAN(LAN lan) {
		context.add(lan);
		LANs.add(lan);
	}

	public static void addPerson(Person p) {
		context.add(p);
		people.add(p);
	}
}
