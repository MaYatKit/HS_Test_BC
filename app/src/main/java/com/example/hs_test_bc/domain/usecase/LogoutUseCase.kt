package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun execute(): Flow<Boolean> = flow {
        try {
            authRepository.clearAuthInfo()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}
