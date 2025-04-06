package com.example.hs_test_bc.data.repository

import com.example.hs_test_bc.data.remote.api.GitHubApi
import com.example.hs_test_bc.data.remote.model.RepositoryResponse
import com.example.hs_test_bc.data.remote.model.SearchRepositoriesResponse
import com.example.hs_test_bc.data.remote.model.UserResponse
import com.example.hs_test_bc.data.repositoryImpl.GitHubRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq
import kotlin.jvm.java
import kotlin.test.assertFailsWith

class GitHubRepositoryImplTest {

    @Test
    fun `searchRepositories returns expected result when API call is successful`() = runTest {
        val mockGitHubService = mock(GitHubApi::class.java)
        val repository = GitHubRepositoryImpl(mockGitHubService)

        val expectedResponse = SearchRepositoriesResponse(
            total_count = 2,
            incomplete_results = false,
            items = listOf(
                RepositoryResponse(
                    id = 1,
                    name = "test",
                    full_name = "",
                    owner = UserResponse("", 1, "", "", "", "", ""),
                    html_url = "",
                    description = "",
                    fork = false,
                    url = "",
                    created_at = "",
                    updated_at = "",
                    pushed_at = "",
                    homepage = null,
                    size = 1,
                    stargazers_count = 1,
                    watchers_count = 1,
                    language = "",
                    forks_count = 1,
                    open_issues_count = 1,
                    license = null,
                    topics = null,
                    default_branch = ""
                )
            )
        )

        `when`(
            mockGitHubService.searchRepositories(
                eq("testQuery"),
                eq("stars"),
                eq("desc"),
                eq(1),
                anyInt()
            )
        ).thenReturn(expectedResponse)

        repository.searchRepositories("testQuery").collect {
            assertNotNull(it)
            assertEquals(expectedResponse.total_count, it.total_count)
            assertEquals("test", it.items.first().name)
        }
        verify(mockGitHubService).searchRepositories("testQuery", "stars", "desc", 1)
    }

    @Test
    fun `searchRepositories should catch exception when API call fails`() = runTest {
        val mockGitHubService = mock(GitHubApi::class.java)
        val repository = GitHubRepositoryImpl(mockGitHubService)

        `when`(
            mockGitHubService.searchRepositories(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenThrow(RuntimeException("Test error"))

        repository.searchRepositories("testQuery").collect {
            assertNotNull(it)
            assertEquals(0, it.total_count)
            assertEquals(true, it.incomplete_results)
            assertEquals(0, it.items.size)
        }

        verify(mockGitHubService).searchRepositories("testQuery", "stars", "desc", 1)
    }
}