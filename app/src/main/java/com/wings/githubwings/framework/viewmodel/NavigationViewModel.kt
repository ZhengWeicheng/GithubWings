package com.wings.githubwings.framework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.network.base.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T, R> NetworkResult<T>.toCommonUIState(
    onSuccess: (T) -> R
): CommonUIState<R> {
    return when (this) {
        is NetworkResult.Success -> {
            CommonUIState.Success(onSuccess(this.data))
        }

        is NetworkResult.Error -> {
            CommonUIState.Error(this.exception, this.code, this.message)
        }

        is NetworkResult.Loading -> {
            CommonUIState.Loading
        }

        is NetworkResult.Idle -> {
            CommonUIState.Idle
        }
    }
}

/**
 * Compose导航通用ViewModel基类
 * @param T 网络请求数据类型
 * @param R Repository实现类
 */
abstract class NavigationViewModel<T : com.wings.githubwings.framework.network.base.BaseRepository, R> :
    ViewModel() {
    var repository: T? = null
        get() {
            if (field == null) {
                field = createRepository()
            }
            return field
        }
    val _commonUIState =
        MutableStateFlow<CommonUIState<R>>(CommonUIState.Idle)
    val commonUIState: StateFlow<CommonUIState<R>> = _commonUIState.asStateFlow()
    abstract fun createRepository(): T

    // 导航参数
    private val _navigationParams = MutableStateFlow<Map<String, Any>>(emptyMap())
    val navigationParams: StateFlow<Map<String, Any>> = _navigationParams.asStateFlow()

    /**
     * 带Loading状态的网络请求
     */
    fun <S> safeRequestWithLoading(
        block: suspend () -> NetworkResult<S>,
    ): Flow<NetworkResult<S>> = flow {
        emit(NetworkResult.Loading)
        try {
            val result = block()
            emit(result)
        } catch (e: Exception) {
            emit(
                NetworkResult.Error(
                    e,
                    null,
                    e.message
                )
            )
        }
    }

    /**
     * 无Loading状态的网络请求
     */
    fun <R> safeRequest(
        block: suspend () -> NetworkResult<R>,
    ): Flow<NetworkResult<R>> = flow {
        try {
            emit(block())
        } catch (e: Exception) {
            emit(
                NetworkResult.Error(
                    e,
                    null,
                    e.message
                )
            )
        }
    }

    /**
     * 设置导航参数
     */
    fun setNavigationParams(params: Map<String, Any>) {
        viewModelScope.launch {
            _navigationParams.value = params
        }
    }

    /**
     * 获取导航参数
     */
    fun getNavigationParam(key: String): Any? {
        return _navigationParams.value[key]
    }

    open fun retry() {

    }
}
