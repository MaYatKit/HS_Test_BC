package com.example.hs_test_bc.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.BuildConfig
import com.example.hs_test_bc.domain.usecase.CheckAuthStatusUseCase
import com.example.hs_test_bc.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            checkAuthStatusUseCase.execute().catch {
                _uiState.value = LoginUiState.NotAuthenticated
            }.collect { isAuthenticated ->
                _uiState.value = if (isAuthenticated) {
                    LoginUiState.Authenticated
                } else {
                    LoginUiState.NotAuthenticated
                }
            }
        }
    }

    fun getAuthUrl(): String {
        val scopes = "repo,user"
        return "https://github.com/login/oauth/authorize" +
                "?client_id=${BuildConfig.CLIENT_ID}" +
                "&scope=$scopes" +
                "&redirect_uri=${BuildConfig.REDIRECT_SCHEME}://${BuildConfig.REDIRECT_HOST}"
    }
}

sealed class LoginUiState {
    data object Loading : LoginUiState()
    data object Authenticated : LoginUiState()
    data object NotAuthenticated : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}