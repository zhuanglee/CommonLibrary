package cn.lzh.utils;

import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要算法5
 */
public final class MD5Util {
	private MD5Util() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * MD5编码
	 * @param text
	 * @return
	 */
	@NonNull
	public static String digest(@NonNull String text) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digests = md5.digest(text.getBytes());
		return ByteUtil.toHexString(digests);
	}

}
