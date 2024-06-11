package sifely.utils;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 */
public class OkHttpPostClient {

    private final OkHttpClient client = (OkHttpClient) OkHttpClientObject.CLIENT.getClientInstance();



    public static Map<String, String> postJsonHttp(String url, String json, Map<String, String> heads){
        MediaType mediaType = MediaType.parse("application/json");  // 设置 MediaType

        RequestBody requestBody = RequestBody.create(mediaType , json);  // 创建 RequestBody 对象

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)  // 设置请求方法为 POST，并传入 RequestBody
                .build();  // 创建 Request 对象

        return doPost(url, heads, requestBody);
    }

    public static Map<String, String> postUrlencodedHttp(String url, Map <String,Object> param, Map<String, String> heads) {


        StringBuilder builder = new StringBuilder();

        param.forEach((name, value) -> {
            if (builder.length() != 0) {
                builder.append('&');
            }
            builder.append(name);
            if (value != null) {
                builder.append('=');
                builder.append(value);
            }

        });

        //return builder.toString();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/x-www-form-urlencoded"), builder.toString());
        return doPost(url, heads, requestBody);
    }

    private static Map<String, String> doPost(String url, Map<String, String> heads, RequestBody requestBody) {
        // 创建一个OkHttpClient对象
        OkHttpClient okHttpClient  = new OkHttpClient
                .Builder()
                .connectTimeout(20, TimeUnit.SECONDS)  // 设置连接超时为 20 秒
                .readTimeout(60, TimeUnit.SECONDS)     // 设置读取超时为 60 秒
                .writeTimeout(60, TimeUnit.SECONDS)    // 设置写入超时为 60 秒
                .build();

        // 创建一个请求对象
        Request request = new Request.Builder().url(url).post(requestBody).headers(Headers.of(heads)).build();
        // 发送请求获取响应
        try {
            //log.info("[invoke_api] url:{}, heads:{}", url, heads);
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
            }
            // 判断请求是否成功
            Map<String, String> resultMap = new HashMap<>();

            resultMap.put("statusCode", String.valueOf(response.code()));
            resultMap.put("responseContent", response.body().string());
            resultMap.put("message", response.message());

            return resultMap;
        } catch (IOException e) {
        }
        return new HashMap<>();
    }

}
