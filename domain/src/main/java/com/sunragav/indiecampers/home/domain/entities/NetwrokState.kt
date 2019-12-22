package com.sunragav.indiecampers.home.domain.entities

import com.jakewharton.rxrelay2.BehaviorRelay
import javax.inject.Inject
import javax.inject.Singleton

data class NetworkState(
    val status: Status,
    val msg: String? = null
) {

    enum class Status {
        RUNNING,
        SUCCESS_LOADED, // New
        SUCCESS_EMPTY, // New
        FAILED,
        DISCONNECTED,
        CONNECTED
    }

    companion object {

        val EMPTY = NetworkState(Status.SUCCESS_EMPTY) // New
        val LOADED = NetworkState(Status.SUCCESS_LOADED) // New
        val LOADING = NetworkState(Status.RUNNING)
        val ERROR = NetworkState(Status.FAILED)
        val CONNECTED = NetworkState(Status.CONNECTED)
        val DISCONNECTED = NetworkState(Status.DISCONNECTED)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

@Singleton
class NetworkStateRelay @Inject constructor() {
    val relay: BehaviorRelay<NetworkState> = BehaviorRelay.create()
}