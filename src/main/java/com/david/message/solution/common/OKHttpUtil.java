package com.david.message.solution.common;


import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * 利用okhttp进行get和post的访问
 *
 * @author cp
 *
 */
public class OKHttpUtil {

    /**
     * 发起get请求,参数写在url中
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .addHeader("content-type", "application/json;charset=UTF-8")
                    .build();

            return getHttpResponseString(client, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发起get请求，
     * @param url
     * @param paramMap 参数集合
     * @return
     */
    public static String httpGetParam(String url,Map<String,Object> paramMap) {
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request.Builder reqBuilder = new Request.Builder();
            HttpUrl.Builder paramBuilder = HttpUrl.parse(url).newBuilder();
            for(String key:paramMap.keySet()) {
                paramBuilder.addQueryParameter(key,paramMap.get(key).toString());
            }
            reqBuilder.url(paramBuilder.build()).addHeader("content-type", "application/json;charset=UTF-8");
            Request request = reqBuilder.build();
            return getHttpResponseString(client, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getHttpResponseString(OkHttpClient client, Request request) {
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url, String json) {
        String result = null;
        try {
            OkHttpClient httpClient = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}