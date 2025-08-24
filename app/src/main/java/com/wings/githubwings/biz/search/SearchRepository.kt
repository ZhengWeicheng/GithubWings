package com.wings.githubwings.biz.search

import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.RepoSearchResponse

class SearchRepository(private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java)!!) :
    BaseRepository() {

    suspend fun searchRepositories(query: String, page: Int): NetworkResult<RepoSearchResponse> {
        return safeApiCall {
            apiService.searchRepositories(query = query, page = page)
        }
    }
}