package com.wings.githubwings.biz.home

import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.RepoSearchResponse

class HomeRepository(private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java)) :
    BaseRepository() {
    suspend fun fetchTrendingRepos(page: Int): NetworkResult<RepoSearchResponse> {
        return safeApiCall {
            apiService.searchRepositories(page = page)
        }
    }
}