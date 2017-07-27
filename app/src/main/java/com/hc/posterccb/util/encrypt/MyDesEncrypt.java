package com.hc.posterccb.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by alex on 2017/4/1.
 */

public class MyDesEncrypt {
    private byte[] desKey;

    public MyDesEncrypt(String desKey) {
        this.desKey = desKey.getBytes();
    }

    public String desDecrypt(String decodeString) throws Exception {
        //使用指定密钥构造IV
        IvParameterSpec iv = new IvParameterSpec(desKey);
        //根据给定的字节数组和指定算法构造一个密钥。
        SecretKeySpec skeySpec = new SecretKeySpec(desKey, "DES");
        //返回实现指定转换的 Cipher 对象
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        //解密初始化
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        //解码返回

        byte[] byteMi = BASE64.decode(decodeString.toCharArray());
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }
}
