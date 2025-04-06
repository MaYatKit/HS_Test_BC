package com.example.hs_test_bc.ui.screen.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.model.Issue
import com.example.hs_test_bc.domain.usecase.CreateIssueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateIssueViewModel @Inject constructor(
    private val createIssueUseCase: CreateIssueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateIssueUiState>(CreateIssueUiState.Initial)
    val uiState: StateFlow<CreateIssueUiState> = _uiState

    fun createIssue(
        owner: String,
        repo: String,
        title: String,
        body: String
    ) {
        if (title.isBlank()) {
            _uiState.value = CreateIssueUiState.Error("Title cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateIssueUiState.Loading

            createIssueUseCase(owner, repo, title, body)
                .catch { e ->
                    _uiState.value =
                        CreateIssueUiState.Error(e.localizedMessage ?: "Unknown error occurred")
                }
                .collectLatest { issue ->
                    _uiState.value = CreateIssueUiState.Success(issue)
                }
        }
    }

    fun resetState() {
        _uiState.value = CreateIssueUiState.Initial
    }
}

sealed class CreateIssueUiState {
    data object Initial : CreateIssueUiState()
    data object Loading : CreateIssueUiState()
    data class Success(val issue: Issue) : CreateIssueUiState()
    data class Error(val message: String) : CreateIssueUiState()
}