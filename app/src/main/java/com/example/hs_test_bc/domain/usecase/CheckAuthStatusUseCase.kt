package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun execute(): Flow<Boolean> {
        return authRepository.isLoggedIn()
    }
}
