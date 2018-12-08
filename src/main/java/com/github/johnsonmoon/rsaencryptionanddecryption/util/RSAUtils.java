package com.github.johnsonmoon.rsaencryptionanddecryption.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Create by johnsonmoon at 2018/12/6 11:25.
 */
public class RSAUtils {
    private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    private static final String ALGORITHM = "RSA";
    private static final Integer KEY_SIZE_DEFAULT = 4096;
    private static KeyPairGenerator keyPairGenerator;
    private static KeyFactory keyFactory;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    public static class MyKeyPair {
        public PrivateKey privateKey;
        public PublicKey publicKey;
    }

    /**
     * Create public/private key pair.
     *
     * @param keySize size of the key
     * @return {@link MyKeyPair}
     */
    public static synchronized MyKeyPair createKeyPair(Integer keySize) {
        if (keySize == null) {
            keySize = KEY_SIZE_DEFAULT;
        }
        keyPairGenerator.initialize(keySize, new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        MyKeyPair myKeyPair = new MyKeyPair();
        myKeyPair.publicKey = publicKey;
        myKeyPair.privateKey = privateKey;
        return myKeyPair;
    }

    /**
     * Encode byte array by Base64 format.
     *
     * @param source source byte data
     * @return base64 encoded string
     */
    public static String base64Encode(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    /**
     * Decode base64 encoded string by Base64 format.
     *
     * @param encoded encoded string
     * @return source byte array
     */
    public static byte[] base64Decode(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }

    /**
     * Convert public key from byte array to {@link PublicKey}
     *
     * @param keyBytes key byte array
     * @return {@link PublicKey}
     */
    public static PublicKey getPublicKey(byte[] keyBytes) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Convert private key from byte array to {@link PrivateKey}
     *
     * @param keyBytes key byte array
     * @return {@link PrivateKey}
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Encrypt source by public key.
     *
     * @param sourceBytes source
     * @param publicKey   {@link PublicKey}
     * @return encrypted data
     */
    public static byte[] encryptByPublicKey(byte[] sourceBytes, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int pageSize = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;//该密钥能够加密的最大字节长度
            List<Byte[]> bytesList = split(sourceBytes, pageSize);
            List<Byte> result = new ArrayList<>();
            for (Byte[] bytes : bytesList) {
                result.addAll(convert(cipher.doFinal(copy(bytes))));
            }
            return convert(result);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Encrypt source by private key.
     *
     * @param sourceBytes source
     * @param privateKey  {@link PrivateKey}
     * @return encrypted data
     */
    public static byte[] encryptByPrivateKey(byte[] sourceBytes, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            int pageSize = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8 - 11;
            List<Byte[]> bytesList = split(sourceBytes, pageSize);
            List<Byte> result = new ArrayList<>();
            for (Byte[] bytes : bytesList) {
                result.addAll(convert(cipher.doFinal(copy(bytes))));
            }
            return convert(result);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Decrypt by public key
     *
     * @param encryptedSource encrypted source
     * @param publicKey {@link PublicKey}
     * @return source
     */
    public static byte[] decryptByPublicKey(byte[] encryptedSource, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            int pageSize = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;//该密钥能够解密的最大字节长度
            List<Byte[]> bytesList = split(encryptedSource, pageSize);
            List<Byte> result = new ArrayList<>();
            for (Byte[] bytes : bytesList) {
                result.addAll(convert(cipher.doFinal(copy(bytes))));
            }
            return convert(result);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Decrypt by private key.
     *
     * @param encryptedSource encrypt source
     * @param privateKey {@link PrivateKey}
     * @return source
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedSource, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int pageSize = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            List<Byte[]> bytesList = split(encryptedSource, pageSize);
            List<Byte> result = new ArrayList<>();
            for (Byte[] bytes : bytesList) {
                result.addAll(convert(cipher.doFinal(copy(bytes))));
            }
            return convert(result);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    private static List<Byte[]> split(byte[] bytes, int pageSize) {
        int remain = bytes.length % pageSize;
        int pages = remain != 0 ? bytes.length / pageSize + 1 : bytes.length / pageSize;
        List<Byte[]> bytesList = new ArrayList<>();
        Byte[] temp;
        for (int page = 0; page < pages; page++) {
            if (page == pages - 1 && remain != 0) {
                temp = new Byte[remain];
                System.arraycopy(copy(bytes), page * pageSize, temp, 0, remain);
            } else {
                temp = new Byte[pageSize];
                System.arraycopy(copy(bytes), page * pageSize, temp, 0, pageSize);
            }
            bytesList.add(temp);
        }
        return bytesList;
    }

    private static Byte[] copy(byte[] bytes) {
        Byte[] bytesU = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesU[i] = bytes[i];
        }
        return bytesU;
    }

    private static byte[] copy(Byte[] bytes) {
        byte[] bytesL = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesL[i] = bytes[i];
        }
        return bytesL;
    }

    private static byte[] convert(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    private static List<Byte> convert(byte[] bytes) {
        List<Byte> byteList = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
            byteList.add(b);
        }
        return byteList;
    }
}
