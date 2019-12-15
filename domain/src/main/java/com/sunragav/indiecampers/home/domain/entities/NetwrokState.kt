package com.sunragav.indiecampers.home.domain.entities

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {

    enum class Status {
        RUNNING,
        SUCCESS_LOADED, // New
        SUCCESS_EMPTY, // New
        FAILED
    }

    companion object {

        val EMPTY = NetworkState(Status.SUCCESS_EMPTY) // New
        val LOADED = NetworkState(Status.SUCCESS_LOADED) // New
        val LOADING = NetworkState(Status.RUNNING)
        val ERROR = NetworkState(Status.FAILED)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}