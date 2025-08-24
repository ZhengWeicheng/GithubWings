package com.wings.githubwings

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.BaseCore
import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.biz.login.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _authState = MutableStateFlow<Boolean>(false)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()
    val loginService = ServiceCenter.getService<ILoginApi>()!!

    init {
        viewModelScope.launch {
            _authState.value = loginService.isLoggedIn()
        }

    }

    fun handleGithubAuthCode(code: String) {
        viewModelScope.launch {
            val result = LoginRepository().handleGithubAuthCode(code)
            when (result) {
                is NetworkResult.Success -> {
                    _authState.value = true
                    Toast.makeText(
                        BaseCore.app,
                        "Login Success", Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Error -> {
                    _authState.value = false
                    Toast.makeText(
                        BaseCore.app,
                        "Login Failed", Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginService.clearAuthInfo()
            _authState.value = false
        }
    }
}