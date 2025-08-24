package com.wings.githubwings.model.api

import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.model.bean.IssueReq
import com.wings.githubwings.model.bean.IssueResp
import com.wings.githubwings.model.bean.RepoSearchResponse
import com.wings.githubwings.model.bean.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubServiceApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String = "stars:>1000",
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): NetworkResult<RepoSearchResponse>

    @GET("repositories")
    suspend fun getPublicRepositories(
        @Query("since") since: Int? = null,
        @Query("per_page") perPage: Int = 30
    ): NetworkResult<List<GitHubRepo>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoDetail(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): NetworkResult<GitHubRepo>

    @GET("user")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): NetworkResult<User>

    @GET("user/repos")
    suspend fun getUserRepositories(
        @Header("Authorization") token: String,
        @Query("sort") sort: String = "updated",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): NetworkResult<List<GitHubRepo>>

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body issue: IssueReq
    ): NetworkResult<IssueResp>


}