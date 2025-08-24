package com.wings.githubwings.framework.network

import com.wings.githubwings.framework.network.base.NetClientConfig
import okhttp3.Interceptor

class RetrofitNetConfig(
    override val baseUrl: String,
    requestInterceptor: Interceptor = AppInterceptor()
) : NetClientConfig(baseUrl)