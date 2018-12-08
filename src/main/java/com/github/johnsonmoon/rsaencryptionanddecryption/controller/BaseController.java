package com.github.johnsonmoon.rsaencryptionanddecryption.controller;

import com.github.johnsonmoon.rsaencryptionanddecryption.common.RSAKeyPairCache;
import com.github.johnsonmoon.rsaencryptionanddecryption.entity.UploadParam;
import com.github.johnsonmoon.rsaencryptionanddecryption.util.RSAUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Create by johnsonmoon at 2018/12/6 10:33.
 */
@RestController
@RequestMapping("/base")
public class BaseController {
    @GetMapping("/pub_key")
    public String getPublicKey() {
        return RSAKeyPairCache.getInstance().getPublicKeyBase64();
    }

    @PostMapping("/upload")
    public String decryption(@RequestBody UploadParam uploadParam) {
        String text = uploadParam.getText();
        byte[] decrypted = RSAUtils.decryptByPrivateKey(
                RSAUtils.base64Decode(text),
                RSAUtils.getPrivateKey(RSAUtils.base64Decode(RSAKeyPairCache.getInstance().getPrivateKeyBase64())));
        return new String(decrypted == null ? "".getBytes() : decrypted);
    }
}
