package com.wings.githubwings.framework.network.base

/**
 * Generic network result wrapper class
 */
sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val exception: Throwable,
        val code: Int? = null,
        val message: String? = null
    ) : NetworkResult<Nothing>()

    object Idle : NetworkResult<Nothing>()
}