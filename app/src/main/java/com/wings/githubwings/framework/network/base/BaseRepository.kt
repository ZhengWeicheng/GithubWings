package com.wings.githubwings.framework.network.base

import com.wings.githubwings.framework.service.center.ServiceCenter

fun <T> getService(clazz: Class<T>): T {
    val networkClient = ServiceCenter.getService<INetworkClient>()
    if (networkClient == null) {
        throw IllegalStateException("INetworkClient service is not registered in ServiceCenter")
    }
    return networkClient.get(clazz)
}

fun <T> getService(clazz: Class<T>, baseUrl: String): T {
    val networkClient = ServiceCenter.getService<INetworkClient>()
    if (networkClient == null) {
        throw IllegalStateException("INetworkClient service is not registered in ServiceCenter")
    }
    return networkClient.get(clazz, baseUrl)
}

open class BaseRepository {
    /**
     * 封装网络请求，确保所有Repository请求都经过try-catch处理
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> NetworkResult<T>): NetworkResult<T> {
        return try {
            apiCall()
        } catch (throwable: Throwable) {
            NetworkResult.Error(
                exception = throwable,
                message = throwable.message ?: "Unknown error occurred"
            )
        }
    }
}