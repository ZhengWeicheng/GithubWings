package com.wings.githubwings.framework.network.base

data class NetworkConfig(
    public val baseUrl: String,
    public val connectTimeOut: Long = 30,
    public val timeOut: Long = 30
) {
}