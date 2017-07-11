package com.hc.posterccb.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by alex on 2017/7/8.
 */

public class TUtil {
    public static <T> T getT(Object o, int i) {

        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                return ((Class<T>) ((ParameterizedType) type).getActualTypeArguments()[i]).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
