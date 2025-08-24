package com.wings.githubwings.biz.mine

import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.BaseCore
import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel
import com.wings.githubwings.framework.viewmodel.toCommonUIState
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.model.bean.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MineUIContent {
    var user: User? = null
    val repositoriesList = mutableListOf<GitHubRepo>()
    var hasMoreRepos: Boolean = true
    var isLoadingMore = false
}

class MineViewModel : NavigationViewModel<MineRepository, MineUIContent>() {
    private val _content = MineUIContent()
    private val loginService = ServiceCenter.getService<ILoginApi>()!!
    private var currentPage = 1
    private var user: User? = null

    init {
        initUserInfo()
        getUserRepositories()
    }

    fun initUserInfo() {
        val userJson = loginService.getCurrentUser()
        user = if (userJson.isNotEmpty()) {
            BaseCore.baseGson.fromJson(userJson, User::class.java)
        } else {
            null
        }
        _content.user = user
    }

    private fun getUserRepositories(page: Int = 1) {
        viewModelScope.launch {
            if (_content.user == null) {
                val result = repository!!.getCurrentUser()
                if (result !is NetworkResult.Success) {
                    if (result is NetworkResult.Error) {
                        _commonUIState.value = CommonUIState.Error(result.exception)
                    }
                    return@launch
                }
                user = result.data
                _content.user = result.data
            }
            if (page == 1) {
                _content.repositoriesList.clear()
                _content.hasMoreRepos = false
                _content.isLoadingMore = false
                currentPage = 1
            }
            safeRequest {
                repository!!.getUserRepositories(page)
            }.collectLatest { result ->
                _commonUIState.value = result.toCommonUIState { data ->
                    _content.repositoriesList.addAll(data)
                    _content.hasMoreRepos = data.isNotEmpty()
                    _content.isLoadingMore = false
                    _content.user = user
                    if (data.isNotEmpty()) {
                        currentPage = page + 1
                    }
                    _content
                }
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_content.hasMoreRepos
                && commonUIState.value is CommonUIState.Success
                && !_content.isLoadingMore
            ) {
                getUserRepositories(currentPage)
            }
        }
    }

    override fun createRepository(): MineRepository {
        return MineRepository()
    }
}