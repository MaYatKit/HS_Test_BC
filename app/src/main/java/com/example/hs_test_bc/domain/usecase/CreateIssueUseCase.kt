package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.data.mapper.toDomain
import com.example.hs_test_bc.domain.model.Issue
import com.example.hs_test_bc.domain.repository.AuthRepository
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateIssueUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val authRepository: AuthRepository
) {

    operator fun invoke(
        owner: String,
        repo: String,
        title: String,
        body: String
    ): Flow<Issue> = flow {
        val token = authRepository.getAccessToken().first()
        if (token != null) {
            gitHubRepository.createIssue(token, owner, repo, title, body)
                .map { it.toDomain() }
                .collect { emit(it) }
        } else {
            throw IllegalStateException("User not authenticated")
        }
    }

}
