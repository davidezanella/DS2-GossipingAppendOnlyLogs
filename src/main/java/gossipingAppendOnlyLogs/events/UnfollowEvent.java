package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnfollowEvent extends Event {

    public final PersonPublicKey unfollowedPerson;

    public UnfollowEvent(PersonPublicKey unfollowedPerson) {
        this.unfollowedPerson = unfollowedPerson;
    }
}
