package gossipingAppendOnlyLogs.eventGeneration;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.events.StreamEvent;
import gossipingAppendOnlyLogs.events.UnblockEvent;
import gossipingAppendOnlyLogs.events.UnfollowEvent;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
import gossipingAppendOnlyLogs.events.BlockEvent;
import gossipingAppendOnlyLogs.events.Event;
import gossipingAppendOnlyLogs.events.FollowEvent;
import repast.simphony.engine.schedule.ScheduleParameters;
import gossipingAppendOnlyLogs.RepastUtils;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

public class EventGenerationStrategyWithInterests extends EventGenerationStrategy {

	protected EventGenerationStrategyWithInterests(Person person) {
		super(person);
		var params = ScheduleParameters.createUniformProbabilityRepeating(10, 0, 100, 50, 1);
		scheduler.schedule(params, this, "generateEvent");
		scheduler.schedule(params, this, "generateInterestEvent");
	}

	/*every now and then, create a stream event (this makes sure that a person actively 
	 *posts new events and doesn't only follow/unfollow other members of the system 
	 */
	@Override
	protected void generateEvent() {
		var event = new StreamEvent("Some content");
		person.addEventToPersonalLog(event);
	}
	
	
	protected void generateInterestEvent() {
		//make a follow, unfollow, block or unblock event
		//in order to make this more realistic these events don't have all the same probabilites
		Random rand = new Random();
		int eventChosen = rand.nextInt(10);
		Boolean eventDone = false;
		
		while(!eventDone) {
			Event event = null;
			if(eventChosen <= 4)
				//a followEvent is the most probable event type
				event = createFollowEvent();
			else if (eventChosen <= 7) 
				event = createUnfollowEvent();
			else if(eventChosen <= 8)
				event = createBlockEvent();
			else {
				event = createUnblockEvent();
			}
			
			if(event != null) {
				person.addEventToPersonalLog(event);
				eventDone = true;
			}
			
			//in the case that an event wasn't complete (e.g. unblock event when the person never
			//blocked someone, revert the event to a follow event
			eventChosen = 0;
		}
	}
	
	protected FollowEvent createFollowEvent() {
		PersonPublicKey persToFollow = null;
		
		//get a set with the pkeys of everyone
		Set<PersonPublicKey> persons = RepastUtils
	        .getAllPeopleInGrid(person)
	        .stream()
	        .map(availPers -> availPers.getPublicKey())
	        .collect(Collectors.toSet());
		
		//set with all already followed persons
		Set <PersonPublicKey> following = person
			.getStore()
			.getLogsFollowedBy(person.getPublicKey());
		
		Set <PersonPublicKey> blocked = person
			.getStore()
			.getLogsBlockedBy(person.getPublicKey());
		
		//I don't want to start following persons Im already following or have blocked
		persons.removeAll(following);
		persons.removeAll(blocked);
		
		int item = new Random().nextInt(persons.size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		
		for(PersonPublicKey key : persons)
		{
		    if (i == item)
		        persToFollow = key;
		    i++;
		}
		
		return new FollowEvent(persToFollow);
	}
	
	protected UnfollowEvent createUnfollowEvent() {
		PersonPublicKey persToUnfollow = null;
		
		//set with all already followed persons
		Set <PersonPublicKey> following = person
			.getStore()
			.getLogsFollowedBy(person.getPublicKey());
		
		// TODO: can I follow a person I've blocked?
		Set <PersonPublicKey> blocked = person
			.getStore()
			.getLogsBlockedBy(person.getPublicKey());
		
		following.removeAll(blocked);
		
		int item = new Random().nextInt(following.size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		
		for(PersonPublicKey key : following)
		{
		    if (i == item)
		        persToUnfollow = key;
		    i++;
		}
		
		return new UnfollowEvent(persToUnfollow);
	}
	
	protected BlockEvent createBlockEvent() {
		PersonPublicKey persToBlock = null;
		
		//get a set with the pkeys of everyone
		Set<PersonPublicKey> persons = RepastUtils
	        .getAllPeopleInGrid(person)
	        .stream()
	        .map(availPers -> availPers.getPublicKey())
	        .collect(Collectors.toSet());
		
		Set <PersonPublicKey> blocked = person
			.getStore()
			.getLogsBlockedBy(person.getPublicKey());
		
		persons.removeAll(blocked);
		
		int item = new Random().nextInt(persons.size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		
		for(PersonPublicKey key : persons)
		{
		    if (i == item)
		    	persToBlock = key;
		    i++;
		}
		
		return new BlockEvent(persToBlock);
	}
	
	protected UnblockEvent createUnblockEvent() {
		PersonPublicKey persToUnblock = null;
		
		Set <PersonPublicKey> blocked = person
			.getStore()
			.getLogsBlockedBy(person.getPublicKey());
		
		int item = new Random().nextInt(blocked.size()); // In real life, the Random object should be rather more shared than this
		int i = 0;
		
		for(PersonPublicKey key : blocked)
		{
		    if (i == item)
		    	persToUnblock = key;
		    i++;
		}
		
		return new UnblockEvent(persToUnblock);
	}
}

