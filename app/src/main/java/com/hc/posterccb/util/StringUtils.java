package com.hc.posterccb.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by alex on 2017/6/30.
 */

public class StringUtils {
    public static String setEncoding(String res,String encodType){
        try {
            res=new String(res.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}
