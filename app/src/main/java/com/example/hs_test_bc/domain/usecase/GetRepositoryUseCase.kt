package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.data.mapper.toDomain
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.domain.repository.AuthRepository
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for getting a detail repository.
 */
@Singleton
class GetRepositoryUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val authRepository: AuthRepository
) {
    fun execute(owner: String, repo: String): Flow<Repository> = flow {
        val token = authRepository.getAccessToken().first()
        if (token != null) {
            gitHubRepository.getUserRepository(token = token, owner = owner, repo = repo).map {
                it.toDomain()
            }.collect { emit(it) }
        }else {
            gitHubRepository.getRepository(owner, repo).map {
                it.toDomain()
            }.collect { emit(it) }
        }
    }
}
