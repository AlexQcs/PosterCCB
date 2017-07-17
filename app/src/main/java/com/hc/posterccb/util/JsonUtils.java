package com.hc.posterccb.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public static <T> ArrayList<T> JsonStr2ArrayList(String jsonStr,Object o){
        Gson gson=new Gson();

        ArrayList<T> resList= gson.fromJson(jsonStr,new TypeToken<ArrayList<T>>(){}.getType());
        return resList;
    }
}
