package com.wings.githubwings.biz.home

import androidx.lifecycle.viewModelScope
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
    var totalPages = 1
    var isLoadingMore = false
    var hasMoreData = true
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
                _content.totalPages = 1
            }
            safeRequestWithLoading(
                block = {
                    repository!!.fetchTrendingRepos(page = page)
                }).collectLatest { result ->
                _commonUIState.value = result.toCommonUIState { data ->
                    _content.repositoriesList.addAll(data.items)
                    _content.currentPage = page
                    _content.totalPages = data.total_count
                    _content.hasMoreData = data.incomplete_results
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
        if (_content.hasMoreData && !_content.isLoadingMore) {
            fetchTrendingRepos(_content.currentPage + 1)
        }
    }

    override fun createRepository(): HomeRepository {
        return HomeRepository()
    }
}