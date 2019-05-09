package com.xy.utility;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xy.onlineteam.common.constvar.FlagConstVar;
import com.xy.onlineteam.common.utility.AESUtils;
import com.xy.onlineteam.common.utility.RSAUtils;
import com.xy.onlineteam.common.utility.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ProtocolUtil {

    private static Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

    public static byte[] compress_encrypt_tokenV1(byte[] dataBytes, byte nz, byte crypt, String privateKey) {
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);

            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PRIVATE) {
                dataBytes = RSAUtils.encryptByPrivateKey(dataBytes, privateKey);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PRIVATE_OAEPPADDING) {
                dataBytes = RSAUtils.encryptByPrivateKey(dataBytes, privateKey, "RSA/ECB/OAEPPadding");
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }

        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] decrypt_decompress_tokenV1(String reqUrl, byte[] dataBytes, String privateKey) {
        try {
            if (reqUrl.contains("v1")) {
                dataBytes = RSAUtils.decryptByPrivateKey(dataBytes, privateKey, "RSA/ECB/OAEPPadding", 2048);
            } else {
                dataBytes = RSAUtils.decryptByPrivateKey(dataBytes, privateKey, "RSA/ECB/PKCS1Padding", 2048);
            }
            dataBytes = ZipUtil.unGzip(dataBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBytes;
    }

    public static byte[] compress_encrypt_tokenV2(byte[] dataBytes, byte nz, byte crypt, String privateKey) {
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);

            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PRIVATE) {
                dataBytes = RSAUtils.encryptByPrivateKey(dataBytes, privateKey);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PRIVATE_OAEPPADDING) {
                dataBytes = RSAUtils.encryptByPrivateKey(dataBytes, privateKey, "RSA/ECB/OAEPPadding");
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] decrypt_decompress_tokenV2(byte[] dataBytes, byte nz, byte crypt, String privateKey) {
        try {
            if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PUBLIC) {
                dataBytes = RSAUtils.decryptByPrivateKey(dataBytes, privateKey);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_RSA_PUBLIC_OAEPPADDING) {
                dataBytes = RSAUtils.decryptByPrivateKey(dataBytes, privateKey, "RSA/ECB/OAEPPadding", 2048);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.debug("crypt flag is invalid.");
            }
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.unGzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }
    public static byte[] compress_encrypt_baseV101(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes){
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 ) {
                dataBytes = AESUtils.aesEncrypt(dataBytes,aesKeyBytes,"AES/ECB/PKCS5Padding",null);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] decrypt_decompress_baseV101(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes) {
        try {
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 ) {
                dataBytes = AESUtils.aesDecrypt(dataBytes, aesKeyBytes, "AES/ECB/PKCS5Padding", null);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("rypt flag is invalid.");
            }
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.unGzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }
    public static byte[] compress_encrypt_baseV1(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes) {
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 || crypt == FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes = AESUtils.aes128Encrypt(dataBytes, aesKeyBytes);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] compress_encrypt_baseV2(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes,
                                                 byte[] aesIVBytes) {
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 || crypt == FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes = AESUtils.aesEncrypt(dataBytes, aesKeyBytes, AESUtils.ALGORITHM, aesIVBytes);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }
    public static byte[] decrypt_decompress_baseV1(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes){
        try {
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 || crypt == FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes = AESUtils.aes128Decrypt(dataBytes, aesKeyBytes);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.unGzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }
    public static byte[] decrypt_decompress_baseV2(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes,
                                                   byte[] aesIVBytes) {
        try {
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 || crypt == FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes = AESUtils.aesDecrypt(dataBytes, aesKeyBytes, AESUtils.ALGORITHM, aesIVBytes);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("rypt flag is invalid.");
            }
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.unGzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] compress_encrypt_newBaseV101(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes,String KEY_ALGORITHM) {
        try {
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.gzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128 || crypt == FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes=AESUtils.aesEncrypt(dataBytes,aesKeyBytes,KEY_ALGORITHM,null);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("crypt flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static byte[] decrypt_decompress_newBaseV101(byte[] dataBytes, byte nz, byte crypt, byte[] aesKeyBytes,String KEY_ALGORITHM) {
        try {
            if (crypt == FlagConstVar.CRYPT_FLAG_AES128||crypt==FlagConstVar.CRYPT_FLAG_AES256) {
                dataBytes = AESUtils.aesDecrypt(dataBytes, aesKeyBytes, KEY_ALGORITHM, null);
            } else if (crypt == FlagConstVar.CRYPT_FLAG_NO) {
            } else {
                logger.error("rypt flag is invalid.");
            }
            if (nz == FlagConstVar.ZIP_FLAG_GZIP) {
                dataBytes = ZipUtil.unGzip(dataBytes);
            } else if (nz == FlagConstVar.ZIP_FLAG_NO) {
            } else {
                logger.error("nz flag is invalid.");
            }
        } catch (Exception e) {
            logger.error("Exception", e.getMessage());
        }
        return dataBytes;
    }

    public static String map_transform_json(Map<String, String> map) {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.putAll(map);
        return mJSONObject.toJSONString();
    }

    public static String map2_transform_json(Map<String, List<String>> map) {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.putAll(map);
        return mJSONObject.toJSONString();
    }

    public static String map3_transform_json(Map<String, Object> map) {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.putAll(map);
        return mJSONObject.toJSONString();
    }

    public static String map_transform_jsonArray(List<Object> list) {
        JSONArray mJSONArray=new JSONArray();
        mJSONArray.addAll(list);
        return  mJSONArray.toJSONString();
    }
}
