package com.wings.githubwings.biz.login

import com.google.gson.Gson
import com.wings.githubwings.BuildConfig
import com.wings.githubwings.framework.BaseCore
import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.BaseRepository
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.network.base.getService
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.model.api.AuthServiceApi
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.AccessTokenResp
import com.wings.githubwings.model.bean.User

class LoginRepository(
    private val apiService: GithubServiceApi = getService(GithubServiceApi::class.java),
    private val authService: AuthServiceApi = getService(
        AuthServiceApi::class.java,
        BuildConfig.authBaseUrl
    )
) : BaseRepository() {

    suspend fun handleGithubAuthCode(code: String): NetworkResult<AccessTokenResp> {
        val loginService = ServiceCenter.getService<ILoginApi>()!!
        try {
            val result = authService.getAccessToken(
                BuildConfig.clientId,
                BuildConfig.clientSecret,
                code,
                BuildConfig.redirectUrl
            )
            if (result is NetworkResult.Success) {
                val accessTokenResp = result.data
                loginService.saveAuthInfo(
                    accessTokenResp.access_token,
                    accessTokenResp.expires_in + System.currentTimeMillis() / 1000
                )
                val user =
                    apiService.getCurrentUser(token = "Bearer ${accessTokenResp.access_token}")
                if (user is NetworkResult.Success) {
                    loginService.setUserName(user.data.login)
                    loginService.saveCurrentUser(
                        BaseCore.baseGson.toJson(
                            user.data,
                            User::class.java
                        )
                    )
                }
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResult.Error(e)
        }
    }

    fun getAuthUrl(): String {
        // 增加更全面的权限范围，解决"Resource not accessible by integration"错误
        // 包含repo权限（用于访问仓库和创建issue）和user权限（用于访问用户信息）
        val scopes = listOf(
            "repo",
            "public_repo",
            "user",
            "issues:write",
            "write:discussion"
        ).joinToString(",")

        return "https://github.com/login/oauth/authorize" +
                "?client_id=${BuildConfig.clientId}" +
                "&scope=$scopes" +
                "&redirect_uri=${BuildConfig.redirectUrl}" +
                "&allow_signup=false" // 禁止注册跳转
    }
}