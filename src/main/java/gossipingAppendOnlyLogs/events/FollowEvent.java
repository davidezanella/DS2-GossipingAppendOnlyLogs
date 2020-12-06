package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class FollowEvent extends Event {

    public final PersonPublicKey followedPerson;

    public FollowEvent(PersonPublicKey followedPerson) {
        this.followedPerson = followedPerson;
    }
}
