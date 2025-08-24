package com.wings.githubwings.framework.login

import kotlinx.coroutines.flow.Flow

interface ILoginApi {
    fun isLoggedIn(): Boolean
    fun getAccessToken(): String
    suspend fun saveAuthInfo(
        accessToken: String,
        expireTime: Long,
        refreshToken: String,
        refreshTokenExpireTime: Long
    )

    fun clearAuthInfo()
    fun getUserName(): Flow<String?>
    fun setUserName(userName: String)
    fun saveCurrentUser(userJson: String)
    fun getCurrentUser(): String
    suspend fun isNeedRefreshToken(): Boolean
}