package com.sunragav.indiecampers.home.domain.entities

import com.jakewharton.rxrelay2.BehaviorRelay
import javax.inject.Inject
import javax.inject.Singleton

data class RepositoryState(
    val status: Status,
    val msg: String? = null
) {

    enum class Status {
        RUNNING,
        SUCCESS_LOADED, // New
        SUCCESS_EMPTY, // New
        FAILED,
        DISCONNECTED,
        CONNECTED,
        DB_ERROR,
        DB_LOADED
    }

    companion object {

        val EMPTY = RepositoryState(Status.SUCCESS_EMPTY) // New
        val LOADED = RepositoryState(Status.SUCCESS_LOADED) // New
        val LOADING = RepositoryState(Status.RUNNING)
        val ERROR = RepositoryState(Status.FAILED)
        val DB_ERROR = RepositoryState(Status.DB_ERROR)
        val DB_LOADED = RepositoryState(Status.DB_LOADED)
        val CONNECTED = RepositoryState(Status.CONNECTED)
        val DISCONNECTED = RepositoryState(Status.DISCONNECTED)
        fun error(msg: String?) = RepositoryState(Status.FAILED, msg)
    }
}

@Singleton
class RepositoryStateRelay @Inject constructor() {
    val relay: BehaviorRelay<RepositoryState> = BehaviorRelay.create()
}