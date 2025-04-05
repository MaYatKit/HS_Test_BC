package com.example.hs_test_bc.data.mapper

import com.example.hs_test_bc.data.remote.model.RepositoryResponse
import com.example.hs_test_bc.data.remote.model.UserResponse
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.domain.model.User

fun RepositoryResponse.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        fullName = full_name,
        owner = User(
            login = owner.login,
            id = owner.id,
            avatarUrl = owner.avatar_url,
            htmlUrl = owner.html_url
        ),
        description = description,
        language = language,
        starsCount = stargazers_count,
        forksCount = forks_count,
        issuesCount = open_issues_count,
        topics = topics,
        htmlUrl = html_url
    )
}

fun UserResponse.toDomain(): User {
    return User(
        login = login,
        id = id,
        avatarUrl = avatar_url,
        htmlUrl = html_url
    )
}