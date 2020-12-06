package gossipingAppendOnlyLogs.models;

import gossipingAppendOnlyLogs.events.BlockEvent;
import gossipingAppendOnlyLogs.events.FollowEvent;
import gossipingAppendOnlyLogs.events.UnblockEvent;
import gossipingAppendOnlyLogs.events.UnfollowEvent;

public class TransitiveInterestLog extends Log {

    public TransitiveInterestLog(PersonPublicKey id) {
        super(id);
    }

    public void follow(PersonKeys keysOfLogOwner, PersonPublicKey id) {
        appendEvent(keysOfLogOwner, new FollowEvent(id));
    }

    public void unfollow(PersonKeys keysOfLogOwner, PersonPublicKey id) {
        appendEvent(keysOfLogOwner, new UnfollowEvent(id));
    }

    public void block(PersonKeys keysOfLogOwner, PersonPublicKey id) {
        appendEvent(keysOfLogOwner, new BlockEvent(id));
    }

    public void unblock(PersonKeys keysOfLogOwner, PersonPublicKey id) {
        appendEvent(keysOfLogOwner, new UnblockEvent(id));
    }
}
