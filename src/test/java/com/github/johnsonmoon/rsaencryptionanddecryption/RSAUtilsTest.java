package com.github.johnsonmoon.rsaencryptionanddecryption;

import com.github.johnsonmoon.rsaencryptionanddecryption.util.RSAUtils;
import org.junit.Test;

/**
 * Create by johnsonmoon at 2018/12/6 17:50.
 */
public class RSAUtilsTest {
    @Test
    public void test() {
        RSAUtils.MyKeyPair myKeyPair = RSAUtils.createKeyPair(null);
        String pub_base64 = RSAUtils.base64Encode(myKeyPair.publicKey.getEncoded());
        String pri_base64 = RSAUtils.base64Encode(myKeyPair.privateKey.getEncoded());

        String origin = "hello!";

        byte[] encrypted_pub = RSAUtils.encryptByPublicKey(origin.getBytes(), RSAUtils.getPublicKey(RSAUtils.base64Decode(pub_base64)));
        byte[] decrypted_pri = RSAUtils.decryptByPrivateKey(encrypted_pub, RSAUtils.getPrivateKey(RSAUtils.base64Decode(pri_base64)));
        System.out.println(new String(decrypted_pri));

        byte[] encrypted_pri = RSAUtils.encryptByPrivateKey(origin.getBytes(), RSAUtils.getPrivateKey(RSAUtils.base64Decode(pri_base64)));
        byte[] decrypted_pub = RSAUtils.decryptByPublicKey(encrypted_pri, RSAUtils.getPublicKey(RSAUtils.base64Decode(pub_base64)));
        System.out.println(new String(decrypted_pub));
    }
}
