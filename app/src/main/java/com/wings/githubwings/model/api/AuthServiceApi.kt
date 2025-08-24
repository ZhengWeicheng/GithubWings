package com.wings.githubwings.model.api

import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.model.bean.AccessTokenResp
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthServiceApi {
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): NetworkResult<AccessTokenResp>
}