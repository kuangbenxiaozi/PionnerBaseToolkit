package com.pioneer.base.toolkit.file;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncodeUtils {

    // 客户端的加密key
    public static final String DEFAULT_KEY = "";
    // 向量
    private static final String IV = "";
    // 加解密统一使用的编码方式
    private static final String ENCODING = "utf-8";
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * 加密
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText,String secretKey) throws Exception {
        return encode(plainText,secretKey,Base64.DEFAULT);
    }

    public static String encode(String plainText,String secretKey,int encodeFlag) throws Exception {
        Key key = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("desede");
        key = secretKeyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE,key,ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64.encodeToString(encryptData,encodeFlag);
    }

    /**
     * 解密
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText,String secretKey) throws Exception {
        Key key = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("desede");
        key = secretKeyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE,key,ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText,Base64.DEFAULT));
        return new String(decryptData,ENCODING);
    }

    /**
     * md5 加密
     * @param string
     * @return
     */
    public static String md5Encode(String string) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(string.getBytes());
            byte bytes[] = md5.digest();
            for(int i = 0; i < bytes.length; i++) {
                String s = Integer.toHexString(bytes[i] & 0xff);
                if(s.length() == 1) {
                    buf.append("0");
                }
                buf.append(s);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public final static String sha1Encode(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
