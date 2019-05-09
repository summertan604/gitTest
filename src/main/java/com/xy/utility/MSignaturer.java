package com.xy.utility;

import com.xy.onlineteam.common.utility.Signaturer;

import java.security.MessageDigest;

public class MSignaturer extends Signaturer {

	/***
	 * SHA256加密 生成64位SHA码
	 * 
	 * @param inStr
	 * @return 返回64位SHA码
	 */
	public static String sha256Encode(String inStr) {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = sha.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Throwable e) {
			return "";
		}
	}

	// public static String sign(String priKeyText, String plainText) {
	// return new String(sign(priKeyText.getBytes(), plainText));
	// }
	//
	// public static byte[] sign(byte[] priKeyText, String plainText) {
	// try {
	// PKCS8EncodedKeySpec e = new
	// PKCS8EncodedKeySpec(Base64.decode(priKeyText));
	// KeyFactory keyf = KeyFactory.getInstance("RSA");
	// PrivateKey prikey = keyf.generatePrivate(e);
	// Signature signet = Signature.getInstance("MD5withRSA");
	// signet.initSign(prikey);
	// signet.update(plainText.getBytes());
	// byte[] signed = Base64.encodeToByte(signet.sign());
	// return signed;
	// } catch (Exception arg6) {
	// System.out.println("签名失败");
	// arg6.printStackTrace();
	// return null;
	// }
	// }
}
