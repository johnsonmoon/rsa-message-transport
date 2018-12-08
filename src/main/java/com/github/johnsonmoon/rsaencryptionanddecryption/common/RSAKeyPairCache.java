package com.github.johnsonmoon.rsaencryptionanddecryption.common;

/**
 * Create by johnsonmoon at 2018/12/6 14:36.
 */
public class RSAKeyPairCache {
    private static RSAKeyPairCache rsaKeyPairCache;

    public static RSAKeyPairCache getInstance() {
        if (rsaKeyPairCache == null) {
            rsaKeyPairCache = new RSAKeyPairCache();
        }
        return rsaKeyPairCache;
    }

    private RSAKeyPairCache() {
    }

    private String publicKeyBase64;
    private String privateKeyBase64;

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPrivateKeyBase64() {
        return privateKeyBase64;
    }

    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }
}
