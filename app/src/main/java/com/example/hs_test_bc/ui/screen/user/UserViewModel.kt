package com.example.hs_test_bc.ui.screen.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.domain.model.User
import com.example.hs_test_bc.domain.usecase.GetUserProfileUseCase
import com.example.hs_test_bc.domain.usecase.GetUserRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private var currentPage = 1

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            try {
                getUserProfileUseCase.execute().collectLatest { user ->
                    loadUserRepositories(user)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    private fun loadUserRepositories(user: User, page: Int = 1) {
        viewModelScope.launch {
            getUserRepositoriesUseCase.execute(page)
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Unknown error occurred")
                }
                .collectLatest { repositories ->
                    val currentRepos = if (page == 1) emptyList() else
                        (_uiState.value as? ProfileUiState.Success)?.repositories ?: emptyList()

                    val newList = currentRepos + repositories

                    _uiState.value = ProfileUiState.Success(
                        user = user,
                        repositories = newList,
                        hasMoreRepos = repositories.isNotEmpty()
                    )
                }
        }
    }

    fun loadMoreRepositories() {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success && currentState.hasMoreRepos) {
            currentPage++
            loadUserRepositories(currentState.user, currentPage)
        }
    }


}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(
        val user: User,
        val repositories: List<Repository>,
        val hasMoreRepos: Boolean
    ) : ProfileUiState()
    data object LoggedOut : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}