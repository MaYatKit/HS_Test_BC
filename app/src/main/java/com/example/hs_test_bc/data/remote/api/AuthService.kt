package com.example.hs_test_bc.data.remote.api

import com.example.hs_test_bc.data.remote.model.AccessTokenResponse
import retrofit2.http.*

interface AuthService {
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): AccessTokenResponse
}