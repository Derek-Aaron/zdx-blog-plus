package com.zdx.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RsaUtil {

    /**
     * 加密
     * @param token  加密内容
     * @return 返回密文
     */
    public static String encrypt(String token) {
        RSA rsa = SecureUtil.rsa(getPrivateKey(), getPublicKey());
        return new String(rsa.encrypt(token, KeyType.PublicKey));
    }

    /**
     * 解密
     * @param data 密文
     * @return 返回
     */
    public static String decrypt(String data) {
        RSA rsa = SecureUtil.rsa(getPrivateKey(), getPublicKey());
        byte[] decrypt = rsa.decrypt(data, KeyType.PrivateKey);
        return new String(decrypt);
    }



    private static String getPrivateKey(){
        InputStream is = ResourceUtil.getStream("dataKey/private.key");
        List<String> stirs = Lists.newArrayList();
        IoUtil.readLines(is, StandardCharsets.UTF_8, stirs);
        stirs.remove(0);
        stirs.remove(stirs.size() -1);
        return String.join("", stirs);
    }

    private static String getPublicKey(){
        InputStream is = ResourceUtil.getStream("dataKey/public.key");
        List<String> stirs = Lists.newArrayList();
        IoUtil.readLines(is, StandardCharsets.UTF_8, stirs);
        stirs.remove(0);
        stirs.remove(stirs.size() -1);
        return String.join("", stirs);
    }
}
