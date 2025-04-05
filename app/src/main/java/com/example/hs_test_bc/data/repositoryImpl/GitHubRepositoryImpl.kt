package com.example.hs_test_bc.data.repositoryImpl

import com.example.hs_test_bc.data.remote.api.GitHubApi
import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of the GitHubRepository interface.
 */
class GitHubRepositoryImpl @Inject constructor(
    private val gitHubApiService: GitHubApi
) : GitHubRepository {

    override fun searchRepositories(
        query: String,
        language: String?,
        sort: String,
        page: Int
    ): Flow<SearchRepositoriesResponse> = flow {
        val finalQuery = if (language == null) query else "$query language:$language"
        try {
            val response = gitHubApiService.searchRepositories(finalQuery, sort, "desc", page)
            emit(response)
        }catch (e: Exception) {
            e.printStackTrace()
            emit(SearchRepositoriesResponse(0, true, emptyList()))
        }
    }


    override fun getPopularRepositories(
        page: Int
    ): Flow<SearchRepositoriesResponse> = flow {
        try {
            val response = gitHubApiService.searchRepositories("popular", page = page)
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(SearchRepositoriesResponse(0, true, emptyList()))
        }
    }


}

