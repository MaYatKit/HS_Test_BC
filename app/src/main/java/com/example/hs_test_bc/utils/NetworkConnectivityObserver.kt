package com.example.hs_test_bc.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Network connectivity observer.
 */
class NetworkConnectivityObserver(context: Context) {

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val status = MutableStateFlow(Status.Unavailable)

    fun observe(): Flow<Status> = status

    fun stopObserve() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            status.value = Status.Available
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            status.value = Status.Losing
        }

        override fun onLost(network: Network) {
            status.value = Status.Lost
        }

        override fun onUnavailable() {
            status.value = Status.Unavailable
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)
    }
}