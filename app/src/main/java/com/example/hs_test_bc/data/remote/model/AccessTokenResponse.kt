package com.example.hs_test_bc.data.remote.model

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val scope: String
)