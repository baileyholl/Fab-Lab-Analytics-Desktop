package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Used to securely store student ID numbers
 */
public class SecureString {
    /*The string contents AFTER hashing the input string*/
    private String contents;

    public SecureString(String stringToEncrypt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(stringToEncrypt.getBytes());
        contents = new String(messageDigest.digest());
    }


}
