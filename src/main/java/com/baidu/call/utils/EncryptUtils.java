package com.baidu.call.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class EncryptUtils {

	private static Key key;
	private static String KEY_STR = "mykey";

	static {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(KEY_STR.getBytes());
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对字符串进行加密，返回BASE64的加密字符串 <功能详细描述>
	 * 
	 * @param str
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getEncryptString(String str) {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		System.out.println(key);
		try {
			byte[] strBytes = str.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return base64Encoder.encode(encryptStrBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 对BASE64加密字符串进行解密 <功能详细描述>
	 * 
	 * @param str
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDecryptString(String str) {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		try {
			byte[] strBytes = base64Decoder.decodeBuffer(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return new String(encryptStrBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String e(String inputText) {
		return md5(inputText);
	}

	/**
	 * 二次加密，应该破解不了了吧？
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText) {
		return sha(md5(inputText));
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * 
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}

	/**
	 * 生成加密文用
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		String name = "xuxinsheng";
//		String password = "LQcjVijK";
//		String encryname = getEncryptString(name);
//		String encrypassword = getEncryptString(password);
//		System.out.println(encryname);
//		System.out.println(encrypassword);
//
//		System.out.println(getDecryptString(encryname));
//		System.out.println(getDecryptString(encrypassword));
//
//		// md5加密测试
//		String md5_1 = md5("123456");
//		String md5_2 = md5("许新生");
//		System.out.println(md5_1 + "\n" + md5_2);
//		// sha加密测试
//		String sha_1 = sha("123456");
//		String sha_2 = sha("许新生");
//		System.out.println(sha_1 + "\n" + sha_2);
//
//	}

}
