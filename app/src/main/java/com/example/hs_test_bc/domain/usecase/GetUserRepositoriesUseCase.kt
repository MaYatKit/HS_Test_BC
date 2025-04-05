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

@Singleton
class GetUserRepositoriesUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val authRepository: AuthRepository
) {

    fun execute(page: Int = 1): Flow<List<Repository>> = flow {
        val token = authRepository.getAccessToken().first()
        if (token != null) {
            gitHubRepository.getUserRepositories(token, page)
                .map { repositories ->
                    repositories.map { it.toDomain() }
                }.collect { emit(it) }
        } else {
            throw IllegalStateException("User not authenticated")
        }
    }

}
