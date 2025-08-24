package com.wings.githubwings.framework.network

import ResponseBodyConverterFactory
import com.wings.githubwings.framework.network.base.INetworkClient
import com.wings.githubwings.framework.log.FLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkLog : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        FLog.debug("Network", message);
    }
}

class RetrofitClient(private var config: RetrofitNetConfig) : INetworkClient {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(config.callTimeout, TimeUnit.SECONDS)
            .connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(config.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)// 连接失败时重试
            .followRedirects(false)// 禁止重定向
            .addInterceptor(RetryInterceptor()) // 添加重试拦截器，默认重试3次
            .addInterceptor(AppInterceptor()) // 添加AppInterceptor
            .addInterceptor(
                HttpLoggingInterceptor(NetworkLog())
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    private val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(ResponseBodyConverterFactory())
            .addConverterFactory(NetworkResultConverterFactory(GsonConverterFactory.create()))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    override fun <T> get(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    override fun <T> get(clazz: Class<T>, baseUrl: String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ResponseBodyConverterFactory())
            .addConverterFactory(NetworkResultConverterFactory(GsonConverterFactory.create()))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(clazz)
    }
}