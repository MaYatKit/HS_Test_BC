package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.data.mapper.toDomain
import com.example.hs_test_bc.domain.model.SearchRepositoriesResult
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for searching repositories.
 */
@Singleton
class SearchRepositoriesUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository
) {

    fun execute(
        query: String,
        language: String? = null,
        page: Int = 1
    ): Flow<SearchRepositoriesResult> {
        return gitHubRepository.searchRepositories(query, language, "stars", page)
            .map { response ->
                SearchRepositoriesResult(
                    totalCount = response.total_count,
                    items = response.items.map { it.toDomain() },
                    nextPage = if (response.items.isNotEmpty()) page + 1 else null
                )
            }
    }
}