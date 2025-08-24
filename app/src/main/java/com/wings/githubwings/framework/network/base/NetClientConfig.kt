package com.wings.githubwings.framework.network.base


open class NetClientConfig(open val baseUrl: String) {
    var callTimeout: Long = 30
    var connectTimeout: Long = 30
    var readTimeout: Long = 30
    var writeTimeout: Long = 30
    var token: String? = null
}

interface INetworkClient {
    fun <T> get(clazz: Class<T>): T

    fun <T> get(clazz: Class<T>, baseUrl: String): T
}