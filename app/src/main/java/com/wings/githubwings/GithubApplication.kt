package com.wings.githubwings

import android.app.Application
import com.wings.githubwings.framework.BaseCore
import com.wings.githubwings.framework.login.ILoginApi
import com.wings.githubwings.framework.mmkv.MMKVInit
import com.wings.githubwings.framework.network.base.INetworkClient
import com.wings.githubwings.framework.network.base.NetworkManager
import com.wings.githubwings.framework.network.RetrofitClient
import com.wings.githubwings.framework.network.RetrofitNetConfig
import com.wings.githubwings.framework.properties.SPManager
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.biz.login.LoginService
import com.wings.githubwings.log.FLogger
import com.wings.githubwings.framework.log.FLog

class GithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseCore.app = this
        FLog.init(FLogger())
        initMMKV()
        initNetworkCenter()
        initServices()
    }

    private fun initServices() {
        ServiceCenter.put(ILoginApi::class.java.name, LoginService())
    }

    private fun initNetworkCenter() {
        val netConfig = RetrofitNetConfig(BuildConfig.httpBaseUrl)
        val netClient = RetrofitClient(netConfig)
        ServiceCenter.put(INetworkClient::class.java.name, NetworkManager(netClient))
    }

    private fun initMMKV() {
        val mmkvInit = MMKVInit()
        mmkvInit.getSpImpl(this, "mmkv")
        SPManager.init(mmkvInit)
    }
}