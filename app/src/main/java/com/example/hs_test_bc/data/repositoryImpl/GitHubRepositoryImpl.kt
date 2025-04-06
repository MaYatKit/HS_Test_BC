package com.example.hs_test_bc.data.repositoryImpl

import com.example.hs_test_bc.data.remote.api.GitHubApi
import com.example.hs_test_bc.data.remote.model.IssueRequest
import com.example.hs_test_bc.data.remote.model.IssueResponse
import com.example.hs_test_bc.data.remote.model.RepositoryResponse
import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import com.example.hs_test_bc.data.remote.model.UserResponse
import com.example.hs_test_bc.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of the GitHubRepository interface.
 * @param gitHubApiService The GitHubApi instance for making API calls.
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
        } catch (e: Exception) {
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

    override fun getRepository(
        owner: String,
        repo: String
    ): Flow<RepositoryResponse> = flow {
        emit(gitHubApiService.getRepository(owner, repo))
    }

    override fun getUserRepository(
        token: String, owner: String, repo: String
    ): Flow<RepositoryResponse> = flow {
        emit(gitHubApiService.getRepository("token $token", owner, repo))
    }

    override fun getUserRepositories(
        token: String,
        page: Int
    ): Flow<List<RepositoryResponse>> = flow {
        emit(gitHubApiService.getUserRepositories("token $token", "updated", page))
    }

    override fun getCurrentUser(token: String): Flow<UserResponse> = flow {
        emit(gitHubApiService.getCurrentUser("token $token"))
    }


    override fun createIssue(
        token: String,
        owner: String,
        repo: String,
        title: String,
        body: String
    ): Flow<IssueResponse> = flow {
        val issueRequest = IssueRequest(title, body)
        emit(gitHubApiService.createIssue("token $token", owner, repo, issueRequest))
    }

}
