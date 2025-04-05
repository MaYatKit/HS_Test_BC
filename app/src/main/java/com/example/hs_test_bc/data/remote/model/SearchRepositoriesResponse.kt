package com.example.hs_test_bc.data.remote.model

/**
 * Data class representing a search response from the GitHub API
 * `search/repositories`.
 *
 * @property total_count The total number of repositories matching the search query.
 * @property incomplete_results Whether or not the search results are incomplete.
 * @property items A list of [RepositoryResponse] objects representing the search results.
 */
data class SearchRepositoriesResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<RepositoryResponse>
) {
    fun hasNextPage(): Boolean {
        return !incomplete_results
    }
}


data class RepositoryResponse(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: UserResponse,
    val html_url: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    val created_at: String,
    val updated_at: String,
    val pushed_at: String,
    val homepage: String?,
    val size: Int,
    val stargazers_count: Int,
    val watchers_count: Int,
    val language: String?,
    val forks_count: Int,
    val open_issues_count: Int,
    val license: LicenseResponse?,
    val topics: List<String>?,
    val default_branch: String
)

data class UserResponse(
    val login: String,
    val id: Long,
    val avatar_url: String,
    val url: String,
    val html_url: String,
    val repos_url: String,
    val type: String
)


data class LicenseResponse(
    val key: String,
    val name: String,
    val url: String?
)


