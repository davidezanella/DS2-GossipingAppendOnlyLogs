package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class BlockEvent extends Event {
    public final PersonPublicKey blockedPerson;

    public BlockEvent(PersonPublicKey blockedPerson) {
        this.blockedPerson = blockedPerson;
    }
}
