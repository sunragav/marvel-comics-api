package com.sunragav.indiecampers.android_utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class ConnectivityMonitorLiveData @Inject constructor(private val mContext: Context) :
    LiveData<Boolean?>() {
    private lateinit var networkCallback: NetworkCallback

    private lateinit var networkReceiver: NetworkReceiver
    private var connectivityManager: ConnectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("ObsoleteSdkInt")
    override fun onActive() {
        super.onActive()
        updateConnection()
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        } else {
            mContext.registerReceiver(
                networkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkReceiver)
        }
    }

    internal inner class NetworkCallback(private val mConnectionStateMonitor: ConnectivityMonitorLiveData) :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mConnectionStateMonitor.postValue(true)
        }

        override fun onLost(network: Network) {
            mConnectionStateMonitor.postValue(false)
        }

    }

    private fun updateConnection() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            postValue(true)
        } else {
            postValue(false)
        }
    }

    internal inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                updateConnection()
            }
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            networkCallback = NetworkCallback(this)
        } else {
            networkReceiver = NetworkReceiver()
        }
    }
}