package com.hc.posterccb.util.encrypt;

import android.util.Log;

import com.hc.posterccb.Constant;

/**
 * Created by alex on 2017/4/1.
 */

public class DesDecUtils {
    //解密代码
    public static String desDec(String decStr) {
        String[] array = decStr.split("");
        String decKeyHead = array[0] + array[1] + array[2];
        MyDesEncrypt myDesEncrypt = new MyDesEncrypt(decKeyHead + Constant.LICENSE_KEY_FOOT);
//        Log.d("key", decKeyHead + decKeyFoot);
        String finalCecStr = "nVQlz8jD1lQxAmWud5jD2A==";
        finalCecStr = decStr.substring(2, decStr.length());
        String resbyte = "";
//        Log.e("密码", finalCecStr);
        try {
            resbyte = myDesEncrypt.desDecrypt(finalCecStr);
//            Log.e("解密结果", resbyte);
        } catch (Exception e) {
            Log.e("授权:", "False");
        }
        return resbyte;
    }
}
