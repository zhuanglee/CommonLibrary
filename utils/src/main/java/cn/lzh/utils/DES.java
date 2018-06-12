package cn.lzh.utils;

import java.security.Key;

import javax.crypto.Cipher;

public final class DES {
	public static final String DEFAULT_KEY = "cn.lzh.des";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;
	private static DES des = null;

	public static DES getInstance() throws Exception {
		if (des == null) {
			des = new DES();
		}
		return des;
	}

	private DES() throws Exception {
		this(DEFAULT_KEY);
	}

	private DES(String strKey) throws Exception {
		// Security.addProvider(null);
		Key key = getKey(strKey.getBytes());
		encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	public byte[] encrypt(byte[] bytes) throws Exception {
		return encryptCipher.doFinal(bytes);
	}

	public String encrypt(String str) throws Exception {
		return ByteUtil.toHexString(encrypt(str.getBytes()));
	}

	public byte[] decrypt(byte[] bytes) throws Exception {
		return decryptCipher.doFinal(bytes);
	}

	public String decrypt(String str) throws Exception {
		return new String(decrypt(ByteUtil.hex2byte(str)));
	}

	private Key getKey(byte[] bytes) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] keyBytes = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < bytes.length && i < keyBytes.length; i++) {
			keyBytes[i] = bytes[i];
		}
		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(keyBytes, "DES");
		return key;
	}

	public static void main(String[] args) {
		try {
			String test = "测试DES加密";
			// 注意这里，自定义的加密的KEY要和解密的KEY一致，这就是钥匙，如果你上锁了，却忘了钥匙，那么是解密不了的
			DES des = new DES();
			System.out.println("加密前的字符：" + test);
			System.out.println("加密后的字符：" + des.encrypt(test));
			System.out.println("解密后的字符：" + des.decrypt(des.encrypt(test)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}