package gossipingAppendOnlyLogs.events;

import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class UnblockEvent extends Event {
    public final PersonPublicKey unblockedPerson;

    public UnblockEvent(PersonPublicKey unblockedPerson) {
        this.unblockedPerson = unblockedPerson;
    }

    @Override
    protected EventContentHash hashContent() {
        return new EventContentHash(unblockedPerson.hash());
    }
}
