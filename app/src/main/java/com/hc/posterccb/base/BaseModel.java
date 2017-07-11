package com.hc.posterccb.base;

import com.hc.posterccb.http.Http;
import com.hc.posterccb.http.HttpService;
import com.hc.posterccb.mvp.IModel;

/**
 * Created by alex on 2017/7/8.
 */

public class BaseModel implements IModel {
    protected static HttpService httpService;



    //初始化httpService
    static {
        httpService = Http.getHttpService();
    }
}
