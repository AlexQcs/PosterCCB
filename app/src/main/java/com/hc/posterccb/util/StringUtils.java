package com.hc.posterccb.util;

import android.support.annotation.Nullable;

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

    public static boolean isEmpty(@Nullable String str){
        if (str==null||str.length()==0)
            return true;
        else
            return  false;
    }
}
