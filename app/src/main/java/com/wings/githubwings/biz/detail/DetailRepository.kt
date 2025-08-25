package com.wings.githubwings.biz.detail

import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.GitHubRepo

class DetailRepository(private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java)) :
    BaseRepository() {
    suspend fun getRepoDetail(owner: String, repo: String): NetworkResult<GitHubRepo> {
        return safeApiCall {
            val token = ServiceCenter.getService<ILoginApi>()!!.getAccessToken()
            apiService.getRepoDetail(token = token, owner = owner, repo = repo)
        }
    }
}