package webshop;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Convert {

    private Convert(){}

    /**
	 * Helper function to convert byte-Array to String
	 * @param hash
	 * @return given byte-Array as String
	 */
	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

    /**
     * Helper function to convert plain Text String to SHA-256 String
     * @param plain
     * @return SHA-256 String of the given plain Text String
     * @throws NoSuchAlgorithmException
     */
    public static String stringToHex(String plain) throws NoSuchAlgorithmException{
        return Convert.bytesToHex(MessageDigest.getInstance("SHA-256")
		    .digest(plain.getBytes(StandardCharsets.UTF_8)));
    }

}