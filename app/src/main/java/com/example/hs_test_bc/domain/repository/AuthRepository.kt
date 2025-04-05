package com.example.hs_test_bc.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getAccessToken(): Flow<String?>

    fun isLoggedIn(): Flow<Boolean>

    fun exchangeCodeForToken(code: String): Flow<String>

    suspend fun saveAuthInfo(accessToken: String, userName: String)

    suspend fun clearAuthInfo()
}