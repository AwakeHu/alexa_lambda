package sifely.utils;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public enum OkHttpClientObject {

    CLIENT;

    private OkHttpClient clientInstance;

    private Integer connectTimeout_time = 10;
    private Integer writeTimeout_time = 15;
    private Integer readTimeout_time = 15;

    OkHttpClientObject() {
        clientInstance = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout_time, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout_time, TimeUnit.SECONDS)
                .readTimeout(readTimeout_time, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(60, 10L, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .eventListenerFactory(OkhttpEventListener.FACTORY)
                .build();
    }

    public OkHttpClient getClientInstance() {
        return clientInstance;
    }
}
