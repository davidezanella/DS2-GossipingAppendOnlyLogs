package gossipingAppendOnlyLogs;

import gossipingAppendOnlyLogs.events.Event;
import gossipingAppendOnlyLogs.events.EventSignature;
import gossipingAppendOnlyLogs.models.PersonKeys;
import gossipingAppendOnlyLogs.models.PersonPrivateKey;
import gossipingAppendOnlyLogs.models.PersonPublicKey;

public class CryptographyUtils {

    public static PersonKeys generateKeys() {
        return new PersonKeys(null, null);
    }

    public static EventSignature sign(PersonPrivateKey privateKey, Event event) {
        throw new RuntimeException("not implemented yet");
    }

    public static String verifySignature(PersonPublicKey publicKey, String signature) {

    }

    public static String encrypt(String encryptionKey, String message) {}

    public static String decrypt(String decryptionKey, String encryptedMessage) {}
}
