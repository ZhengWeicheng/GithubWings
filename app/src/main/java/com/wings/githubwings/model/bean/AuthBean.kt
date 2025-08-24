package com.wings.githubwings.model.bean

data class AccessTokenResp(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val expires_in: Long,
)