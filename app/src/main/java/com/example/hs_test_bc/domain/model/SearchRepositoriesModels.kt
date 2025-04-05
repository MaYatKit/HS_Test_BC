package com.example.hs_test_bc.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: User,
    val description: String?,
    val language: String?,
    val starsCount: Int,
    val forksCount: Int,
    val issuesCount: Int,
    val topics: List<String>?,
    val htmlUrl: String
)

data class User(
    val login: String,
    val id: Long,
    val avatarUrl: String,
    val htmlUrl: String
)


data class SearchRepositoriesResult(
    val totalCount: Int,
    val items: List<Repository>,
    val hasMoreData: Boolean = false,
    val nextPage: Int?
)