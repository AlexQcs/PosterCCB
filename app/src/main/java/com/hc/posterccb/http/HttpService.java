package com.hc.posterccb.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by alex on 2017/7/8.
 * 网络请求的接口都在这里
 */

public interface HttpService {

    @POST
    Observable<ResponseBody> polling(@Url String url,@Header("command")String command,@Header("factory")String factory, @Header("mac")String mac);

    @POST
    Observable<ResponseBody> report(@Url String url,@Header("command")String command, @Header("mac")String mac, @Body RequestBody xmlStr );

    @POST
    Observable<ResponseBody> downLoad(@Url String url);

    @POST
    Observable<ResponseBody> timesync(@Url String url,@Header("command")String command, @Header("mac")String mac);

    @POST
    Observable<ResponseBody> getModel(@Url String url);

//    @POST("/xmlserver/revXml")
//    Observable<ResponseBody>
}
