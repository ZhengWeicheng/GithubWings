package com.wings.githubwings.biz.login

import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel


class LoginViewModel : NavigationViewModel<LoginRepository, Boolean>() {

    init {
        _commonUIState.value = CommonUIState.Success(true)
    }

    override fun createRepository(): LoginRepository {
        return LoginRepository()
    }

    fun getAuthUrl(): String {
        return repository!!.getAuthUrl()
    }
}