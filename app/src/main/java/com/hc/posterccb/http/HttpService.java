package com.hc.posterccb.http;

import okhttp3.ResponseBody;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by alex on 2017/7/8.
 * 网络请求的接口都在这里
 */

public interface HttpService {

    @POST("/xmlserver/revXml")
    Observable<ResponseBody> polling(@Header("command")String command, @Header("mac")String mac);

//    @POST("/xmlserver/revXml")
//    Observable<ResponseBody>
}
