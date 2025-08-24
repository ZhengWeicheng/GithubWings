package com.wings.githubwings.framework.viewmodel

sealed class CommonUIState<out T> {
    object Loading : CommonUIState<Nothing>()
    data class Success<out T>(val data: T) : CommonUIState<T>()
    data class Error(
        val exception: Throwable,
        val code: Int? = null,
        val message: String? = null
    ) : CommonUIState<Nothing>()

    object Idle : CommonUIState<Nothing>()
}