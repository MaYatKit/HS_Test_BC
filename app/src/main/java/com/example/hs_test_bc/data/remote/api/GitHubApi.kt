package com.example.hs_test_bc.data.remote.api

import com.example.hs_test_bc.data.remote.model.IssueRequest
import com.example.hs_test_bc.data.remote.model.IssueResponse
import com.example.hs_test_bc.data.remote.model.RepositoryResponse
import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import com.example.hs_test_bc.data.remote.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): SearchRepositoriesResponse


    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepositoryResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepositoryResponse

    @GET("user")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): UserResponse

    @GET("user/repos")
    suspend fun getUserRepositories(
        @Header("Authorization") token: String,
        @Query("sort") sort: String = "updated",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<RepositoryResponse>

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body issue: IssueRequest
    ): IssueResponse
}
