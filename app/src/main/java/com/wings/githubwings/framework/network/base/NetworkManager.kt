package com.wings.githubwings.framework.network.base

import com.wings.githubwings.framework.service.center.AbsService

class NetworkManager(private val networkClient: INetworkClient) : AbsService(), INetworkClient {

    override fun <T> get(clazz: Class<T>): T {
        return networkClient.get(clazz)
    }

    override fun <T> get(clazz: Class<T>, baseUrl: String): T {
        return networkClient.get(clazz, baseUrl)
    }
}