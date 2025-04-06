package com.example.hs_test_bc.data.remote.model

data class IssueResponse(
    val id: Long,
    val number: Int,
    val title: String,
    val user: UserResponse,
    val state: String,
    val created_at: String,
    val body: String?
)


data class IssueRequest(
    val title: String,
    val body: String,
    val labels: List<String>? = null
)