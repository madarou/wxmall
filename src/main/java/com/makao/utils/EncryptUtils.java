package com.makao.utils;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.jasypt.util.password.BasicPasswordEncryptor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 * 使用基本的BasicPasswordEncryptor来为密码加密
 */
public class EncryptUtils {
	public static BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
	/** 
     * 创建密匙 
     *  
     * @param algorithm 
     *            加密算法,可用 DES,DESede,Blowfish 
     * @return SecretKey 秘密（对称）密钥 
     */  
    public SecretKey createSecretKey(String algorithm) {  
        // 声明KeyGenerator对象  
        KeyGenerator keygen;  
        // 声明 密钥对象  
        SecretKey deskey = null;  
        try {  
            // 返回生成指定算法的秘密密钥的 KeyGenerator 对象  
            keygen = KeyGenerator.getInstance(algorithm);  
            // 生成一个密钥  
            deskey = keygen.generateKey();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        // 返回密匙  
        return deskey;  
    }  
    
    /** 
    * 根据密匙进行DES加密 
    *  
    * @param key 
    *            密匙 
    * @param info 
    *            要加密的信息 
    * @return String 加密后的信息 
    */  
   public String encryptToDES(SecretKey key, String info) {  
       // 定义 加密算法,可用 DES,DESede,Blowfish  
       String Algorithm = "DES";  
       // 加密随机数生成器 (RNG),(可以不写)  
       SecureRandom sr = new SecureRandom();  
       // 定义要生成的密文  
       byte[] cipherByte = null;  
       try {  
           // 得到加密/解密器  
           Cipher c1 = Cipher.getInstance(Algorithm);  
           // 用指定的密钥和模式初始化Cipher对象  
           // 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)  
           c1.init(Cipher.ENCRYPT_MODE, key, sr);  
           // 对要加密的内容进行编码处理,  
           cipherByte = c1.doFinal(info.getBytes());  
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
       // 返回密文的十六进制形式  
       return byte2hex(cipherByte);  
   }  
   /** 
    * 将二进制转化为16进制字符串 
    *  
    * @param b 
    *            二进制字节数组 
    * @return String 
    */  
   public String byte2hex(byte[] b) {  
       String hs = "";  
       String stmp = "";  
       for (int n = 0; n < b.length; n++) {  
           stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
           if (stmp.length() == 1) {  
               hs = hs + "0" + stmp;  
           } else {  
               hs = hs + stmp;  
           }  
       }  
       return hs.toUpperCase();  
   } 
   
   /** 
    * 根据密匙进行DES解密 
    *  
    * @param key 
    *            密匙 
    * @param sInfo 
    *            要解密的密文 
    * @return String 返回解密后信息 
    */  
   public String decryptByDES(SecretKey key, String sInfo) {  
       // 定义 加密算法,  
       String Algorithm = "DES";  
       // 加密随机数生成器 (RNG)  
       SecureRandom sr = new SecureRandom();  
       byte[] cipherByte = null;  
       try {  
           // 得到加密/解密器  
           Cipher c1 = Cipher.getInstance(Algorithm);  
           // 用指定的密钥和模式初始化Cipher对象  
           c1.init(Cipher.DECRYPT_MODE, key, sr);  
           // 对要解密的内容进行编码处理  
           cipherByte = c1.doFinal(hex2byte(sInfo));  
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
       // return byte2hex(cipherByte);  
       return new String(cipherByte);  
   }  
   
   /** 
    * 十六进制字符串转化为2进制 
    *  
    * @param hex 
    * @return 
    */  
   public byte[] hex2byte(String hex) {  
       byte[] ret = new byte[32];  
       byte[] tmp = hex.getBytes();  
       for (int i = 0; i < 8; i++) {  
           ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
       }  
       return ret;  
   } 
   /** 
    * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF 
    *  
    * @param src0 
    *            byte 
    * @param src1 
    *            byte 
    * @return byte 
    */  
   public static byte uniteBytes(byte src0, byte src1) {  
       byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))  
               .byteValue();  
       _b0 = (byte) (_b0 << 4);  
       byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))  
               .byteValue();  
       byte ret = (byte) (_b0 ^ _b1);  
       return ret;  
   } 
    
	private static final String key0 = "FECOI()*&<MNCXZPKL";  
    private static final Charset charset = Charset.forName("UTF-8");  
    private static byte[] keyBytes = key0.getBytes(charset);  
      
    public static String encode(String enc){  
        byte[] b = enc.getBytes(charset);  
        for(int i=0,size=b.length;i<size;i++){  
            for(byte keyBytes0:keyBytes){  
                b[i] = (byte) (b[i]^keyBytes0);  
            }  
        }  
        return new String(b);  
    }  
      
    public static String decode(String dec){  
        byte[] e = dec.getBytes(charset);  
        byte[] dee = e;  
        for(int i=0,size=e.length;i<size;i++){  
            for(byte keyBytes0:keyBytes){  
                e[i] = (byte) (dee[i]^keyBytes0);  
            }  
        }  
        return new String(e);  
    }  
  
    public static void main(String[] args) {  
    	EncryptUtils jiami = new EncryptUtils();
    	// 生成一个DES算法的密匙  
        SecretKey key = jiami.createSecretKey("DES");  
        System.out.println("key为：" + key);
        // 用密匙加密信息"Hello world!"  
        String str1 = jiami.encryptToDES(key, "Hello");  
        System.out.println("使用des加密信息Hello为:" + str1);  
        // 使用这个密匙解密  
        String str2 = jiami.decryptByDES(key, str1);  
        System.out.println("解密后为：" + str2);  
    }  
}
