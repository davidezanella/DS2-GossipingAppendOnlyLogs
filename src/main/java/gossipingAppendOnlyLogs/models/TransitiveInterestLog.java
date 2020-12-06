package gossipingAppendOnlyLogs.models;

public class TransitiveInterestLog extends Log {

    public TransitiveInterestLog(PersonPublicKey id) {
        super(id);
    }

    public void follow(PersonPrivateKey keyOfLogOwner, PersonPublicKey id) {
        // make sure that this log belongs to the owner
    }

    public void unfollow(PersonPrivateKey keyOfLogOwner, PersonPublicKey id) {
        // make sure that this log belongs to the owner
    }

    public void block(PersonPrivateKey keyOfLogOwner, PersonPublicKey id) {
        // make sure that this log belongs to the owner
    }

    public void unblock(PersonPrivateKey keyOfLogOwner, PersonPublicKey id) {
        // make sure that this log belongs to the owner
    }
}
