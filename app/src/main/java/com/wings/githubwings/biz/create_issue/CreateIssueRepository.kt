package com.wings.githubwings.biz.create_issue

import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.IssueReq
import com.wings.githubwings.model.bean.IssueResp

class CreateIssueRepository(private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java)) :
    BaseRepository() {
    suspend fun createIssue(
        owner: String,
        repo: String,
        issue: IssueReq,
    ): NetworkResult<IssueResp> {
        return safeApiCall {
            val token = ServiceCenter.getService<ILoginApi>()!!.getAccessToken()
            apiService.createIssue(
                "Bearer $token",
                owner,
                repo,
                issue
            )
        }
    }
}