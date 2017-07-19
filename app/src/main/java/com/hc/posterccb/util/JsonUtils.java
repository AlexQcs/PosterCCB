package com.hc.posterccb.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by alex on 2017/7/17.
 */

public class JsonUtils {
    public static <T> String ArrayList2JsonStr(ArrayList<T> list){
        Gson gson=new Gson();
        String result=gson.toJson(list);
        return result;
    }

    public static <T> ArrayList<T> JsonStr2ArrayList(String jsonStr,Type typeOfT){
        Gson gson=new Gson();
        ArrayList<T> resList= gson.fromJson(jsonStr,typeOfT);
        return resList;
    }

    public static <T> String Bean2JsonStr(T t){
        Gson gson=new Gson();
        String result=gson.toJson(t);
        return result;
    }

    public static <T> T JsonStr2Bean(String jsonStr,Type typeOfT){
        Gson gson=new Gson();
        T t=gson.fromJson(jsonStr,typeOfT);
        return t;
    }
}
