package com.wings.githubwings.model.bean

data class AccessTokenResp(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: Long,
    val refresh_token: String,
    val refresh_token_expires_in: Long,
)

data class RefreshTokenResp(
    val access_token: String,
    val refresh_token: String,
    val refresh_token_expires_in: Long,
    val expires_in: Long,
)