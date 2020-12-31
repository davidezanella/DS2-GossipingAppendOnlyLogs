package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class FollowEvent extends Event {

    public final PersonPublicKey followedPerson;

    public FollowEvent(PersonPublicKey followedPerson) {
        this.followedPerson = followedPerson;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(followedPerson.hash());
    }

}
