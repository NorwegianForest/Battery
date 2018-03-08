package com.battery;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by szl on 2018/2/2.
 * 使用OkHttp的接口，便于完成网络操作
 */

public class HttpUtil {

    /**
     * 带有post的http请求
     * http请求一定是在新的线程中执行的，所以有些情况下会用到CountDownLatch类，以等待线程结束
     * @param address 请求的地址，服务器中各Servlet地址详见Constants类
     * @param body post请求数据
     * @param callback 响应结果
     */
    public static void sendRequest(String address, RequestBody body, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 不带post的请求（重载）
     * @param address 请求的地址
     * @param callback 响应
     */
    public static void sendRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
