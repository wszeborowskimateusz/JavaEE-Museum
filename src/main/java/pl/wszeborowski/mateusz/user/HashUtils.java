package pl.wszeborowski.mateusz.user;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hash) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String sha256(String original) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(original.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new java.math.BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
