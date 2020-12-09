package gossipingAppendOnlyLogs.eventGeneration;

import gossipingAppendOnlyLogs.actors.Person;

public class EventGenerationStrategyWithInterests extends EventGenerationStrategy {

	protected EventGenerationStrategyWithInterests(Person person) {
		super(person);
	}

	@Override
	protected void generateEvent() {
		// TODO: generate a StreamEvent, follow/unfollow or block/unblock someone
	}
}
