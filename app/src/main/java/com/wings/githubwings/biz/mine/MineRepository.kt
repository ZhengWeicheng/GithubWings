package com.wings.githubwings.biz.mine

import com.wings.githubwings.framework.BaseCore
import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.model.bean.User

class MineRepository(private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java)) :
    BaseRepository() {

    suspend fun getCurrentUser(): NetworkResult<User> {
        return safeApiCall {
            val token = ServiceCenter.getService<ILoginApi>()!!.getAccessToken()
            val result = apiService.getCurrentUser(token = token)
            if (result is NetworkResult.Success) {
                ServiceCenter.getService<ILoginApi>()!!
                    .saveCurrentUser(BaseCore.baseGson.toJson(result.data))
            }
            result
        }
    }

    suspend fun getUserRepositories(page: Int, perPage: Int = 30): NetworkResult<List<GitHubRepo>> {
        return safeApiCall {
            val token = ServiceCenter.getService<ILoginApi>()!!.getAccessToken()
            apiService.getUserRepositories(token = token, page = page, perPage = perPage)
        }
    }

}