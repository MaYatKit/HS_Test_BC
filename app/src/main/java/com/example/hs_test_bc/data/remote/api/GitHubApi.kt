package com.example.hs_test_bc.data.remote.api

import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import retrofit2.http.GET
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



}