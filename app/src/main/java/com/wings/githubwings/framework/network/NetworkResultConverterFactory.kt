package com.wings.githubwings.framework.network

import com.wings.githubwings.framework.network.base.NetworkResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultConverterFactory(private val delegateFactory: Converter.Factory) :
    Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        // 检查是否是 NetworkResponse 类型
        if (getRawType(type) == NetworkResult::class.java) {
            val dataType = getParameterUpperBound(0, type as ParameterizedType)

            val delegateConverter =
                delegateFactory.responseBodyConverter(dataType, annotations, retrofit)
            return delegateConverter?.let { NetworkResultConverter(it) }
        }
        return null
    }
}