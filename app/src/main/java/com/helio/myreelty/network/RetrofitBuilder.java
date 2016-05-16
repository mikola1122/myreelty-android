package com.helio.myreelty.network;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by markus on 28.01.2016.
 */
public class RetrofitBuilder {
   public static <T> T createRetrofitService(final Class<T> clazz){

        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();

        return restAdapter.create(clazz);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.interceptors().add(chain -> chain.proceed(chain.request()));
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }
}
