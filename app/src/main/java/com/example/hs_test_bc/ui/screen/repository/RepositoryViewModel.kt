package com.example.hs_test_bc.ui.screen.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.domain.usecase.GetRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val getRepositoryUseCase: GetRepositoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RepositoryUiState>(RepositoryUiState.Loading)
    val uiState: StateFlow<RepositoryUiState> = _uiState

    fun loadRepository(owner: String, repo: String) {
        viewModelScope.launch {
            _uiState.value = RepositoryUiState.Loading

            getRepositoryUseCase.execute(owner, repo).catch { e ->
                _uiState.value = RepositoryUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }.collectLatest { repository ->
                _uiState.value = RepositoryUiState.Success(repository)
            }
        }
    }
}

sealed class RepositoryUiState {
    data object Loading : RepositoryUiState()
    data class Success(val repository: Repository) : RepositoryUiState()
    data class Error(val message: String) : RepositoryUiState()
}