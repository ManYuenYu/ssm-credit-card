package com.ioioh.ssm.license.util;

import com.ioioh.ssm.license.domain.License;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-25 7:23 PM
 */
public class RSAUtils extends Base64Utils{

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        return sign(data.getBytes(), privateKey);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }


    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(), publicKey, sign);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }


    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        return new String(decryptByPrivateKey(Base64Utils.decryptBASE64(data), key));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) throws Exception {
        return new String(decryptByPublicKey(Base64Utils.decryptBASE64(data), key));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        return Base64Utils.encryptBASE64(encryptByPublicKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        return Base64Utils.encryptBASE64(encryptByPrivateKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }


    private static String createLicenseString(){
        return License.builder().id(1L).account("Deepexi").productCode("orderServer").expireDate("2020-10-10").version("3.11").build().toString();
    }


    public static void main(String[] args) {
        try {

//            Map<String, Object> map = RSAUtils.initKey();
//            String publicKey = RSAUtils.getPublicKey(map);
//            String privateKey = RSAUtils.getPrivateKey(map);
//            System.out.println("公钥：" + publicKey);
//            System.out.println("私钥：" + privateKey);
//            String data = createLicenseString();
//            System.out.println("原始数据：" + data);
//            System.out.println("==================================================================\n");
//            String encryptData = RSAUtils.encryptByPublicKey(data, publicKey);
//            System.out.println("公钥加密后：" + encryptData);
//            String decryptData = RSAUtils.decryptByPrivateKey(encryptData, privateKey);
//            System.out.println("私钥解密后：" + decryptData);
//            System.out.println("==================================================================\n");
//            String encryptData2 = RSAUtils.encryptByPrivateKey(data, privateKey);
//            System.out.println("私钥加密后：" + encryptData2);
//            String decryptData2 = RSAUtils.decryptByPublicKey(encryptData2, publicKey);
//            System.out.println("公钥解密后：" + decryptData2);
//            System.out.println("==================================================================\n");
//            String sign = RSAUtils.sign(data, privateKey);
//            System.out.println("生成签名：" + sign);
//            boolean verify = RSAUtils.verify(data, publicKey, sign);
//            System.out.println("检验签名结果：" + verify);


            System.out.println("=====================授权码生成=====================");

            Map<String, Object> map = RSAUtils.initKey();
            String publicKey = RSAUtils.getPublicKey(map);
            String privateKey = RSAUtils.getPrivateKey(map);
            System.out.println("公钥：" + publicKey);
            System.out.println("私钥：" + privateKey);

            String data = createLicenseString();
            System.out.println("业务数据：" + data + "\n");

            String sign = RSAUtils.sign(data, privateKey);
            System.out.println("对业务数据生成签名：" + sign);

            Map<String, String> licenseMap = new HashMap<>();
            licenseMap.put("data", data);
            licenseMap.put("sign", sign);
            byte[] bytesOut = changeMapToByte(licenseMap);
            String license = Base64Utils.encryptBASE64(bytesOut);
            System.out.println("授权文件：" + license);


            System.out.println("\n=====================授权验证服务器=====================");
            byte[] bytesIn = Base64Utils.decryptBASE64(license);
            Map<String, String> licenseMap2 = changeByteToMap(bytesIn);
            System.out.println("业务数据" + licenseMap2.get("data") + "\n");
            System.out.println("对业务数据生成签名" + licenseMap2.get("sign"));
            System.out.println("签名验证结果：" + RSAUtils.verify(data, publicKey, sign) + "\n");
            String result = RSAUtils.encryptByPrivateKey("success", privateKey);
            System.out.println("私钥加密授权验证通过结果：" + result);


            System.out.println("\n=====================客户服务器处理验证返回结果=====================");
            System.out.println("公钥解密返回结果" + RSAUtils.decryptByPublicKey(result, publicKey));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static byte[] changeMapToByte(Map<String,String> map) {

        byte[] bytes = null;
        try {
            bytes = JsonSerilizable.serilizableForMap(map).getBytes();
        } catch (Exception e) {
            System.out.println("map到byte[]转换异常");
        }

        return bytes;
    }

    //将byte[]转换成map
    private static Map<String, String> changeByteToMap(byte[] bytes) {

        Map<String, String> retmap = null;

        try {
            if(bytes != null) {
                retmap = JsonSerilizable.deserilizableForMapFromFile(new String(bytes), String.class);
            }else {
                System.out.println("changeByteToMap中bytes为null");
            }

        } catch (Exception e) {
            System.out.println("byte到map转换异常");
        }

        return retmap;
    }



}
