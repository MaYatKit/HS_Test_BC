package com.example.hs_test_bc.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.domain.usecase.LoginUseCase
import com.example.hs_test_bc.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val networkStatus = connectivityObserver.observe()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            NetworkConnectivityObserver.Status.Unavailable
        )


    fun handleOAuthCode(code: String) {
        viewModelScope.launch {
            loginUseCase.execute(code).catch { e ->
                e.printStackTrace()
                Toast.makeText(
                    context, "Login Failed, error = ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }.collect { success ->
                Toast.makeText(
                    context, if (success) "Login Success" else "Login Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun stopObserveNetwork() {
        connectivityObserver.stopObserve()
    }
}
