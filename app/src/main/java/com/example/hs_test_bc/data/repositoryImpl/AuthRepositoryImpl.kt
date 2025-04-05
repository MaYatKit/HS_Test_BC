package com.example.hs_test_bc.data.repositoryImpl

import com.example.hs_test_bc.BuildConfig
import com.example.hs_test_bc.data.local.AuthPreferences
import com.example.hs_test_bc.data.remote.api.AuthService
import com.example.hs_test_bc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of the AuthRepository interface.
 * @param authService The AuthService instance for making API calls.
 * @param authPreferences The AuthPreferences instance for managing authentication preferences.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val authPreferences: AuthPreferences
): AuthRepository {

    override fun getAccessToken(): Flow<String?> {
        return authPreferences.getAccessToken()
    }

    override fun isLoggedIn(): Flow<Boolean> = flow {
        val token = authPreferences.getAccessToken().firstOrNull()
        emit(token != null)
    }

    override fun exchangeCodeForToken(code: String): Flow<String> = flow {
        val response = authService.getAccessToken(
            BuildConfig.CLIENT_ID,
            BuildConfig.CLIENT_SECRET,
            code,
            "${BuildConfig.REDIRECT_SCHEME}://${BuildConfig.REDIRECT_HOST}"
        )
        emit(response.access_token)
    }

    override suspend fun saveAuthInfo(accessToken: String, userName: String) {
        authPreferences.saveAuthInfo(accessToken, userName)
    }

    override suspend fun clearAuthInfo() {
        authPreferences.clearAuthInfo()
    }
}