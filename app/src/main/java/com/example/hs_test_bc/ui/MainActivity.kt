package com.example.hs_test_bc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.hs_test_bc.ui.nav.Navigation
import com.example.hs_test_bc.ui.theme.HS_Test_BCTheme
import com.example.hs_test_bc.utils.NetworkConnectivityObserver.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HS_Test_BCTheme {
                    val snackBarHostState = remember { SnackbarHostState() }

                    LaunchedEffect(true) {
                        viewModel.networkStatus.collectLatest { status ->
                            when (status) {
                                Status.Available -> {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                }
                                Status.Lost -> {
                                    snackBarHostState.showSnackbar(
                                        message = "Connection lost"
                                    )
                                }
                                Status.Losing -> {
                                    snackBarHostState.showSnackbar(
                                        message = "Signal weak"
                                    )
                                }
                                Status.Unavailable -> {
                                    snackBarHostState.showSnackbar(
                                        message = "No connection"
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Navigation(snackBarHostState = snackBarHostState)
                    }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopObserveNetwork()
    }
}
