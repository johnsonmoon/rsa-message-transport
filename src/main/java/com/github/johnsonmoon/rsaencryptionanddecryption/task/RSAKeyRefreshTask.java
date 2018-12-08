package com.github.johnsonmoon.rsaencryptionanddecryption.task;

import com.github.johnsonmoon.rsaencryptionanddecryption.common.RSAKeyPairCache;
import com.github.johnsonmoon.rsaencryptionanddecryption.util.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Create by johnsonmoon at 2018/12/6 10:31.
 */
@Component
public class RSAKeyRefreshTask {
    private static Logger logger = LoggerFactory.getLogger(RSAKeyRefreshTask.class);

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void refreshKey() {
        try {
            RSAUtils.MyKeyPair myKeyPair = RSAUtils.createKeyPair(null);
            RSAKeyPairCache.getInstance().setPublicKeyBase64(RSAUtils.base64Encode(myKeyPair.publicKey.getEncoded()));
            RSAKeyPairCache.getInstance().setPrivateKeyBase64(RSAUtils.base64Encode(myKeyPair.privateKey.getEncoded()));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
