package com.example.hs_test_bc.domain.repository

import com.example.hs_test_bc.data.remote.model.RepositoryResponse
import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing a GitHub repository.
 * Provides methods for searching and etc.
 */
interface GitHubRepository {

    fun searchRepositories(
        query: String,
        language: String? = null,
        sort: String = "stars",
        page: Int = 1
    ): Flow<SearchRepositoriesResponse>

    fun getPopularRepositories(page: Int = 1): Flow<SearchRepositoriesResponse>

    fun getRepository(owner: String, repo: String): Flow<RepositoryResponse>

}
