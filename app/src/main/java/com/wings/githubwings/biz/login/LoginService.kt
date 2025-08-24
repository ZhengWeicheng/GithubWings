package com.wings.githubwings.biz.login

import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.properties.SPProperty
import com.wings.githubwings.framework.service.center.AbsService
import com.wings.githubwings.framework.log.FLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginService : AbsService(), ILoginApi {
    companion object {
        val LoginState: SPProperty<Boolean> = SPProperty("LoginState", false)
        val UserName: SPProperty<String> = SPProperty("UserName", "")
        val AccessToken: SPProperty<String> = SPProperty("AccessToken", "")
        val UserJson: SPProperty<String> = SPProperty("UserJson", "")
        val ExpireTime: SPProperty<Long> = SPProperty("ExpireTime", 0)
    }

    override fun isLoggedIn(): Boolean {
        val expireTime = ExpireTime.getValue()
        FLog.debug("login", "isLoggedIn: $expireTime")
        return expireTime > System.currentTimeMillis() / 1000
    }

    override fun getAccessToken(): String {
        var result = AccessToken.getValue()
        FLog.debug("login", "getAccessToken: $result")
        if (result.isBlank()) {
            return ""
        }
        return "Bearer $result"
    }

    override suspend fun saveAuthInfo(accessToken: String, expireTime: Long) {
        FLog.debug("login", "saveAuthInfo: $accessToken, $expireTime")
        LoginState.setValue(true)
        AccessToken.setValue(accessToken)
        ExpireTime.setValue(expireTime)
    }

    override fun clearAuthInfo() {
        FLog.debug("login", "clearAuthInfo")
        LoginState.setValue(false)
        AccessToken.setValue("")
        UserName.setValue("")
        UserJson.setValue("")
        ExpireTime.setValue(0)
    }

    override fun getUserName(): Flow<String> = flow {
        emit(AccessToken.getValue())
    }

    override fun setUserName(userName: String) {
        UserName.setValue(userName)
    }

    override fun saveCurrentUser(userJson: String) {
        UserJson.setValue(userJson)
    }

    override fun getCurrentUser(): String {
        return UserJson.getValue()
    }
}