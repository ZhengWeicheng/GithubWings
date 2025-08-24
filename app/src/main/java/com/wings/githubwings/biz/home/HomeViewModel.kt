package com.wings.githubwings.biz.home

import androidx.lifecycle.viewModelScope
import com.wings.githubwings.biz.search.SearchUIContent
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel
import com.wings.githubwings.framework.viewmodel.toCommonUIState
import com.wings.githubwings.model.bean.GitHubRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeUIContent {
    val repositoriesList = mutableListOf<GitHubRepo>()
    var currentPage = 1
    var totalCount = 1
    var isLoadingMore = false
    var hasMoreData = true

    // 添加拷贝构造函数，用于创建状态副本
    constructor(source: HomeUIContent) {
        repositoriesList.addAll(source.repositoriesList)
        currentPage = source.currentPage
        totalCount = source.totalCount
        isLoadingMore = source.isLoadingMore
        hasMoreData = source.hasMoreData
    }

    // 无参构造函数
    constructor()
}


class HomeViewModel : NavigationViewModel<HomeRepository, HomeUIContent>() {
    private val _content = HomeUIContent()

    // 独立的刷新状态
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    //登录状态
    private val _isLogin = MutableStateFlow(false)
    val isLogin: StateFlow<Boolean> = _isLogin

    init {
        fetchTrendingRepos()
    }

    private fun fetchTrendingRepos(page: Int = 1) {
        viewModelScope.launch {
            if (isRefreshing.value || page == 1) {
                _isRefreshing.value = true
                _content.repositoriesList.clear()
                _content.currentPage = 1
                _content.totalCount = 1
                _commonUIState.value = CommonUIState.Loading
            } else {
                // 使用拷贝构造函数创建新的状态对象
                val newContent = HomeUIContent(_content).apply {
                    isLoadingMore = true
                }
                // 触发UI更新显示加载状态
                _commonUIState.value = CommonUIState.Success(newContent)
            }

            safeRequest(
                block = {
                    repository!!.fetchTrendingRepos(page = page)
                }).collectLatest { result ->
                _commonUIState.value = result.toCommonUIState { data ->
                    _content.repositoriesList.addAll(data.items)
                    _content.currentPage = page
                    _content.totalCount = data.total_count
                    _content.hasMoreData = data.total_count > _content.repositoriesList.size
                    _content.isLoadingMore = false
                    _isRefreshing.value = false
                    _content
                }
            }
        }
    }

    override fun retry() {
        fetchTrendingRepos(1)
    }

    fun onRefresh() {
        fetchTrendingRepos(1)
    }

    fun onLoadMore() {
        viewModelScope.launch {
            if (_content.hasMoreData
                && commonUIState.value is CommonUIState.Success
                && !_content.isLoadingMore
            ) {
                _content.isLoadingMore = true // 在开始加载前设置加载状态
                _commonUIState.value = CommonUIState.Success(_content) // 触发UI更新显示加载状态
                fetchTrendingRepos(_content.currentPage + 1)
            }
        }
    }

    override fun createRepository(): HomeRepository {
        return HomeRepository()
    }
}