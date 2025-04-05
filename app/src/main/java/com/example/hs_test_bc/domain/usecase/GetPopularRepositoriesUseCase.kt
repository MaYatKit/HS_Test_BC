package com.example.hs_test_bc.domain.usecase

import com.example.hs_test_bc.data.mapper.toDomain
import com.example.hs_test_bc.domain.model.SearchRepositoriesResult
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for getting popular public repositories.
 */
@Singleton
class GetPopularRepositoriesUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository
) {

    fun execute(
        page: Int = 1
    ): Flow<SearchRepositoriesResult> {
        return gitHubRepository.getPopularRepositories(page)
            .map { result ->
                SearchRepositoriesResult(
                    items = result.items.map { it.toDomain() },
                    hasMoreData = result.hasNextPage(),
                    totalCount = result.total_count,
                    nextPage = if (result.hasNextPage()) page + 1 else null
                )
            }
    }
}


