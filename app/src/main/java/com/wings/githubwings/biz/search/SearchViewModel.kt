package com.wings.githubwings.biz.search

import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel
import com.wings.githubwings.framework.viewmodel.toCommonUIState
import com.wings.githubwings.model.bean.GitHubRepo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchUIContent {
    var query = ""
    val repositoriesList = mutableListOf<GitHubRepo>()
    var currentPage = 1
    var totalCount = 0
    var isLoadingMore = false
    var hasMoreRepos = false

    // 添加拷贝构造函数，用于创建状态副本
    constructor(source: SearchUIContent) {
        query = source.query
        repositoriesList.addAll(source.repositoriesList)
        currentPage = source.currentPage
        totalCount = source.totalCount
        isLoadingMore = source.isLoadingMore
        hasMoreRepos = source.hasMoreRepos
    }

    // 无参构造函数
    constructor()
}

class SearchViewModel : NavigationViewModel<SearchRepository, SearchUIContent>() {
    private val _content = SearchUIContent()
    private var _curQueryText = ""

    init {
    }

    fun searchRepositories(queryText: String, page: Int = 1, isLoadMore: Boolean) {
        _curQueryText = queryText
        if (queryText.isEmpty()) {
            restPage()
            return
        }
        viewModelScope.launch {
            if (!isLoadMore) {
                restPage()
                _commonUIState.value = CommonUIState.Loading
            } else {
                // 使用拷贝构造函数创建新的状态对象
                val newContent = SearchUIContent(_content).apply {
                    isLoadingMore = true
                }
                // 触发UI更新显示加载状态
                _commonUIState.value = CommonUIState.Success(newContent)
            }
            safeRequest {
                repository!!.searchRepositories(queryText, page = page)
            }.collectLatest { result ->
                _commonUIState.value = result.toCommonUIState { data ->
                    _content.repositoriesList.addAll(data.items)
                    _content.currentPage = page
                    _content.totalCount = data.total_count
                    // 优化hasMoreRepos的计算逻辑
                    _content.hasMoreRepos =
                        _content.repositoriesList.size < data.total_count && data.items.isNotEmpty()
                    _content.isLoadingMore = false // 重置加载更多状态
                    _content
                }
            }
        }
    }

    private fun restPage() {
        _content.repositoriesList.clear()
        _content.hasMoreRepos = false
        _content.isLoadingMore = false
        _content.currentPage = 1
        // 修复状态更新问题，确保创建新的SearchUIContent对象以触发UI更新
        _commonUIState.value = CommonUIState.Success(_content)
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_content.hasMoreRepos
                && commonUIState.value is CommonUIState.Success
                && !_content.isLoadingMore
            ) {
                _content.isLoadingMore = true // 在开始加载前设置加载状态
                _commonUIState.value = CommonUIState.Success(_content) // 触发UI更新显示加载状态
                searchRepositories(_curQueryText, _content.currentPage + 1, true)
            }
        }
    }

    override fun createRepository(): SearchRepository {
        return SearchRepository()
    }
}