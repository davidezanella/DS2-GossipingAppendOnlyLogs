package gossipingAppendOnlyLogs.eventGeneration;

import gossipingAppendOnlyLogs.actors.Person;
import gossipingAppendOnlyLogs.events.StreamEvent;
import gossipingAppendOnlyLogs.models.PersonPublicKey;
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
			if(eventChosen <= 4) {
				//a followEvent is the most probable event type
				person.addEventToPersonalLog(createFollowEvent());
				eventDone = true;
			} else if (eventChosen <= 7) {
				//TODO: unfollowEvent
			} else if(eventChosen <= 8) {
				//TODO: block event
			} else {
				//TODO: unblock event
			}
			
			//in the case that an event wasn't complete (e.g. unblock event when the person never
			//blocked someone, revert the event to a follow event
			eventChosen = 0;
		}
	}
	
	protected FollowEvent createFollowEvent() {
		Random ran = new Random();
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
}

