package com.example.hs_test_bc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.usecase.CheckAuthStatusUseCase
import com.example.hs_test_bc.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            checkAuthStatusUseCase.execute().collectLatest { isAuthenticated ->
                _isLoggedIn.value = isAuthenticated
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute().collect { success ->
                if (success) {
                    _isLoggedIn.value = false
                }
            }
        }
    }
}