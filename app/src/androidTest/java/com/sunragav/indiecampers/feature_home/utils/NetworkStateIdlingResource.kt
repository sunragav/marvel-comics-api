package com.sunragav.indiecampers.feature_home.utils

import androidx.test.espresso.IdlingResource
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.atomic.AtomicBoolean

class NetworkStateIdlingResource (private val networkStateRelay: NetworkStateRelay) :
    IdlingResource {
    private val isIdle = AtomicBoolean()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var resourceCallback: IdlingResource.ResourceCallback

    override fun getName() =
        "NetworkStateRelayIdlingResource"


    override fun isIdleNow(): Boolean {
        if(isIdle.get())compositeDisposable.dispose()
        return isIdle.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        resourceCallback = callback
        isIdle.getAndSet(false)
        compositeDisposable.addAll(networkStateRelay.relay.observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it == NetworkState.LOADED) {
                Thread.sleep(500)
                isIdle.getAndSet(true)
                resourceCallback.onTransitionToIdle()
            }
        })
    }
}