package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.RepastUtils;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class BlockEvent extends Event {
    public final PersonPublicKey blockedPerson;

    public BlockEvent(PersonPublicKey blockedPerson) {
        this.blockedPerson = blockedPerson;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(blockedPerson.hash());
    }

}
