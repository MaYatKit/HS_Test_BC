package com.example.hs_test_bc.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hs_test_bc.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val networkStatus = connectivityObserver.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NetworkConnectivityObserver.Status.Unavailable)


    fun stopObserveNetwork() {
        connectivityObserver.stopObserve()
    }
}
