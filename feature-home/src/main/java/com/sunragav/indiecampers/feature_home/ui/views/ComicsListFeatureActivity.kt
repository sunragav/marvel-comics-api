package com.sunragav.indiecampers.feature_home.ui.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.utils.ConnectivityState
import dagger.android.AndroidInjection
import javax.inject.Inject

class ComicsListFeatureActivity : AppCompatActivity() {
    @Inject
    lateinit var connectivityState: ConnectivityState

    @Inject
    lateinit var networkStateRelay: NetworkStateRelay

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics_list_feature)
        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        Handler().postDelayed({
            Glide.get(this).clearMemory()

            Thread(Runnable {
                // This method must be called on a background thread.
                Glide.get(this).clearDiskCache()
            }).start()
        }, 3000)
    }


    fun connected() {
        connectivityState.connected.getAndSet(true)
        networkStateRelay.relay.accept(NetworkState.DISCONNECTED)

    }

    fun disconnected() {
        connectivityState.connected.getAndSet(false)
        networkStateRelay.relay.accept(NetworkState.CONNECTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
