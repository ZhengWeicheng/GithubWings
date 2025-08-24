package com.wings.githubwings.framework.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Retrofit重试拦截器
 * 实现默认三次重试机制
 */
class RetryInterceptor(
    private val maxRetry: Int = 3 // 默认重试3次
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var exception: IOException? = null
        var tryCount = 0

        // 当请求失败且重试次数未达到最大次数时，进行重试
        while (tryCount <= maxRetry) {
            try {
                val response = chain.proceed(request)

                // 如果响应成功，直接返回
                if (response.isSuccessful) {
                    return response
                }

                // 对于非成功响应，我们不进行重试，直接返回给上层处理
                return response
            } catch (e: IOException) {
                exception = e

                // 如果超过最大重试次数，抛出异常
                if (tryCount >= maxRetry) {
                    throw e
                }

                // 只对特定的网络异常进行重试
                if (!shouldRetry(e)) {
                    throw e
                }

                // 增加重试计数
                tryCount++
            }
        }

        // 理论上不会执行到这里
        throw exception ?: IOException("Unknown network error")
    }

    /**
     * 判断是否应该重试
     */
    private fun shouldRetry(exception: IOException): Boolean {
        // 只对特定的网络异常进行重试
        return exception is SocketException ||
                exception is SocketTimeoutException ||
                exception is UnknownHostException
    }

    /**
     * 判断是否为网络错误
     */
    private fun isNetworkError(code: Int): Boolean {
        // 仅对特定的HTTP错误码进行重试（如5xx服务器错误）
        return code in 500..599
    }
}