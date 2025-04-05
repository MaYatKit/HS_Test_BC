package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.domain.repository.AuthRepository
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gitHubRepository: GitHubRepository
) {

    fun execute(code: String): Flow<Boolean> = flow {
        try {
            var token: String? = null
            authRepository.exchangeCodeForToken(code).collect {
                token = it
            }

            if (token != null) {
                var userName: String? = null
                gitHubRepository.getCurrentUser(token!!).collect { user ->
                    userName = user.login
                }

                if (userName != null) {
                    // Save auth info (token and username)
                    authRepository.saveAuthInfo(token!!, userName!!)
                    emit(true)
                } else {
                    emit(false)
                }
            } else {
                emit(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(false)
        }
    }
}
