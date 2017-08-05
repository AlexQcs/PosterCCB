package com.hc.posterccb.util.ccbutils;

/**
 * Created by alex on 2017/8/5.
 */

public class JschChannel {
    private static byte[] _$6699 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62,
            -1, 63, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1,
            0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1,
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
            43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

    private String decode(String code)
    {
        if (code == null) {
            return null;
        }
        int len = code.length();
        int pad = 0;
        if (code.charAt(len - 1) == '=') {
            pad++;
        }
        if (code.charAt(len - 2) == '=') {
            pad++;
        }
        int retLen = len / 4 * 3 - pad;
        byte[] ret = new byte[retLen];
        for (int i = 0; i < len; i += 4) {
            int j = i / 4 * 3;
            char ch1 = code.charAt(i);
            char ch2 = code.charAt(i + 1);
            char ch3 = code.charAt(i + 2);
            char ch4 = code.charAt(i + 3);
            int tmp = _$6699[ch1] << 18 | _$6699[ch2] << 12 | _$6699[ch3] << 6 |
                    _$6699[ch4];
            ret[j] = (byte)((tmp & 0xFF0000) >> 16);
            if (i < len - 4) {
                ret[(j + 1)] = (byte)((tmp & 0xFF00) >> 8);
                ret[(j + 2)] = (byte)(tmp & 0xFF);
            } else {
                if (j + 1 < retLen) {
                    ret[(j + 1)] = (byte)((tmp & 0xFF00) >> 8);
                }
                if (j + 2 >= retLen)
                    continue;
                ret[(j + 2)] = (byte)(tmp & 0xFF);
            }
        }
        return new String(ret);
    }
}
