package com.wings.githubwings.biz.detail

import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.viewmodel.NavigationViewModel
import com.wings.githubwings.framework.viewmodel.toCommonUIState
import com.wings.githubwings.model.bean.GitHubRepo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailViewModel : NavigationViewModel<DetailRepository, GitHubRepo>() {

    var owner: String = ""
    var repo: String = ""
    fun getRepoDetail(owner: String, repo: String) {
        this.owner = owner
        this.repo = repo
        viewModelScope.launch {
            safeRequestWithLoading(
                block = {
                    repository!!.getRepoDetail(owner = owner, repo = repo)
                }).collectLatest { result ->
                _commonUIState.value = result.toCommonUIState { data ->
                    data
                }
            }
        }
    }

    override fun retry() {
        getRepoDetail(owner = owner, repo = repo)
    }

    override fun createRepository(): DetailRepository {
        return DetailRepository()
    }
}