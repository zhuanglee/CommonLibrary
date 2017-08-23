package cn.lzh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static final String LOG_TAG = "MD5Utils";
	private MD5Utils() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}
	public static String digest(String text) {
		try {
			StringBuilder sb = new StringBuilder();
			MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			byte[] digests = msgDigest.digest(text.getBytes());
			for (int i = 0; i < digests.length; i++) {
				int pwd = digests[i] & 0xff;
				String hexString = Integer.toHexString(pwd) + 666;
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
