package cn.lzh.utils.security;

import java.security.Key;

import javax.crypto.Cipher;

/**
 * DES
 * @author open source
 */
public final class DES {
	private static final String DEFAULT_KEY = "default";
	private static final String DES = "DES";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	public DES() throws Exception {
		this(DEFAULT_KEY);
	}

	public DES(String strKey) throws Exception {
		// Security.addProvider(null);
		Key key = getKey(strKey.getBytes());
		encryptCipher = Cipher.getInstance(DES);
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		decryptCipher = Cipher.getInstance(DES);
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	private Key getKey(byte[] bytes) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] keyBytes = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < bytes.length && i < keyBytes.length; i++) {
			keyBytes[i] = bytes[i];
		}
		// 生成密钥
		return new javax.crypto.spec.SecretKeySpec(keyBytes, DES);
	}

	public byte[] encrypt(byte[] bytes) throws Exception {
		return encryptCipher.doFinal(bytes);
	}

	public String encrypt(String str) throws Exception {
		return ByteUtil.bytes2hex(encrypt(str.getBytes()));
	}

	public byte[] decrypt(byte[] bytes) throws Exception {
		return decryptCipher.doFinal(bytes);
	}

	public String decrypt(String str) throws Exception {
		return new String(decrypt(ByteUtil.hex2bytes(str)));
	}

}