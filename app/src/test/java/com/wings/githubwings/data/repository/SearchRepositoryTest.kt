package com.wings.githubwings.data.repository

import android.content.Context
import com.wings.githubwings.BuildConfig
import com.wings.githubwings.biz.search.SearchRepository
import com.wings.githubwings.framework.mmkv.MMKVInit
import com.wings.githubwings.framework.network.RetrofitClient
import com.wings.githubwings.framework.network.RetrofitNetConfig
import com.wings.githubwings.framework.network.base.INetworkClient
import com.wings.githubwings.framework.network.base.NetworkManager
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.properties.ISPPropertyInterface
import com.wings.githubwings.framework.properties.SPManager
import com.wings.githubwings.framework.service.center.ServiceCenter
import com.wings.githubwings.model.api.GithubServiceApi
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.model.bean.RepoSearchResponse
import com.wings.githubwings.model.bean.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class SearchRepositoryTest {

    @Mock
    private lateinit var mockApiService: GithubServiceApi

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockApiService = mock(GithubServiceApi::class.java)

        searchRepository = SearchRepository(mockApiService)
    }

    @Test
    fun `searchRepositories should return success result with correct data`() = runTest {
        // Arrange
        val mockRepositories = listOf(
            GitHubRepo(
                id = 1,
                name = "Repo1",
                full_name = "Owner1/Repo1",
                owner = User(
                    login = "Owner1",
                    id = 1,
                    avatar_url = "url1",
                    url = "url1",
                    html_url = "url1",
                    repos_url = "url1",
                    type = "User"
                ),
                html_url = "html_url1",
                description = "Description1",
                fork = false,
                url = "url1",
                created_at = "date1",
                updated_at = "date1",
                pushed_at = "date1",
                homepage = null,
                size = 100,
                stargazers_count = 10,
                watchers_count = 10,
                language = "Kotlin",
                forks_count = 5,
                open_issues_count = 2,
                topics = listOf("topic1", "topic2"),
                default_branch = "main"
            )
        )

        val mockResponse = RepoSearchResponse(
            total_count = 1,
            incomplete_results = false,
            items = mockRepositories
        )

        `when`(
            mockApiService.searchRepositories(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyInt(),
            )
        ).thenReturn(NetworkResult.Success(mockResponse))
        // Act
        val result = searchRepository.searchRepositories("testQuery", 1)

        // Assert
        assertTrue(result is NetworkResult.Success)
        assertNotNull((result as NetworkResult.Success).data)
        assertEquals(1, result.data.total_count)
        assertEquals(1, result.data.items.size)
        assertEquals("Repo1", result.data.items[0].name)

        verify(mockApiService).searchRepositories(anyString(), anyInt().toString())
    }

    @Test
    fun `searchRepositories should handle error and return error result`() = runTest {

        `when`(mockApiService.searchRepositories(anyString(), anyInt().toString()))
            .thenThrow(RuntimeException("Network error"))

        // Act
        val result = searchRepository.searchRepositories("testQuery", 1)

        // Assert
        assertTrue(result is NetworkResult.Error)
        val errorResult = result as NetworkResult.Error
        assertNotNull(errorResult.exception)
        assertEquals("Network error", errorResult.exception.message)

        verify(mockApiService).searchRepositories(anyString(), anyInt().toString())
    }
}