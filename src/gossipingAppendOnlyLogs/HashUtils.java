package gossipingAppendOnlyLogs;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

	public static String hash(String toHash) {
		try {
			var digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
			return new String(hash, StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("cannot compute SHA-256 hash", e);
		}
	}
}
