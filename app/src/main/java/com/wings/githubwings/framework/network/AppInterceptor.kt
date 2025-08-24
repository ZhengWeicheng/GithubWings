package com.wings.githubwings.framework.network

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale


class AppInterceptor() : Interceptor {
    private val userAgent: String = getUserAgent()
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github+json")
            .addHeader("X-GitHub-Api-Version", "2022-11-28")
            .addHeader("User-Agent", userAgent)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun getUserAgent(): String {
        return java.lang.String.format(
            Locale.getDefault(), "github/%s; Android %d; %s; channel/%s",
            "0.0.1",
            Build.VERSION.SDK_INT,
            Locale.getDefault().toString(),
            "official"
        )
    }
}