package com.wings.githubwings.framework.network

import com.wings.githubwings.framework.network.base.NetworkResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException

class NetworkResultConverter<T>(private val delegate: Converter<ResponseBody, T>) :
    Converter<ResponseBody, NetworkResult<T>> {

    override fun convert(value: ResponseBody): NetworkResult<T>? {
        return try {
            val data = delegate.convert(value)
            if (data != null) {
                NetworkResult.Success(data)
            } else {
                NetworkResult.Error(
                    IllegalStateException("Response data is null"),
                    null,
                    "响应数据为空"
                )
            }
        } catch (exception: Exception) {
            when (exception) {
                is HttpException -> {
                    val code = exception.code()
                    val message = exception.message()
                    NetworkResult.Error(exception, code, message)
                }

                is IOException -> {
                    NetworkResult.Error(exception, null, "网络连接错误")
                }

                else -> {
                    NetworkResult.Error(exception, null, exception.message ?: "未知错误")
                }
            }

        }
    }
}