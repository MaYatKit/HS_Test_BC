package com.example.hs_test_bc.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.domain.model.SearchRepositoriesResult
import com.example.hs_test_bc.domain.usecase.GetPopularRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularRepositoriesUseCase: GetPopularRepositoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing

    private val _loadingMore = MutableStateFlow(false)
    val loadingMore: StateFlow<Boolean> = _loadingMore

    private var currentPage = 1
    private val repositoriesList = mutableListOf<Repository>()
    private var hasMoreData = true

    init {
        loadPopularRepositories(initialLoad = true)
    }

    fun loadPopularRepositories(initialLoad: Boolean = false, refresh: Boolean = false) {

        if ((!initialLoad && !refresh && _loadingMore.value) ||
            (refresh && _refreshing.value)) {
            return
        }

        if (initialLoad) {
            _uiState.value = HomeUiState.Loading
            currentPage = 1
            repositoriesList.clear()
        } else if (refresh) {
            _refreshing.value = true
            currentPage = 1
        } else {
            if (!hasMoreData) return
            _loadingMore.value = true
        }

        viewModelScope.launch {
            getPopularRepositoriesUseCase.execute(currentPage).catch { e ->
                if (initialLoad) {
                    _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown error occurred")
                } else if (refresh) {
                    _refreshing.value = false
                } else {
                    _loadingMore.value = false
                }
            }.collectLatest { result ->
                if (initialLoad) {
                    handleInitialLoad(result)
                } else if (refresh) {
                    handleRefresh(result)
                } else {
                    handleLoadMore(result)
                }
            }
        }
    }

    private fun handleInitialLoad(result: SearchRepositoriesResult) {
        repositoriesList.clear()
        repositoriesList.addAll(result.items)
        hasMoreData = result.hasMoreData

        if (repositoriesList.isEmpty()) {
            _uiState.value = HomeUiState.Empty
        } else {
            _uiState.value = HomeUiState.Success(repositoriesList.toList(), hasMoreData)
        }

        if (result.hasMoreData) currentPage++
    }

    private fun handleRefresh(result: SearchRepositoriesResult) {
        _refreshing.value = false

        repositoriesList.clear()
        repositoriesList.addAll(result.items)
        hasMoreData = result.hasMoreData

        if (repositoriesList.isEmpty()) {
            _uiState.value = HomeUiState.Empty
        } else {
            _uiState.value = HomeUiState.Success(repositoriesList.toList(), hasMoreData)
        }

        if (result.hasMoreData) currentPage++
    }

    private fun handleLoadMore(result: SearchRepositoriesResult) {
        _loadingMore.value = false

        repositoriesList.addAll(result.items)
        hasMoreData = result.hasMoreData

        if (repositoriesList.isNotEmpty()) {
            _uiState.value = HomeUiState.Success(repositoriesList.toList(), hasMoreData)
        }

        if (result.hasMoreData) currentPage++
    }

    fun loadNextPage() {
        if (!_loadingMore.value && hasMoreData) {
            loadPopularRepositories(initialLoad = false, refresh = false)
        }
    }

    fun refresh() {
        loadPopularRepositories(initialLoad = false, refresh = true)
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data object Empty : HomeUiState()
    data class Success(val repositories: List<Repository>, val hasMoreData: Boolean) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
