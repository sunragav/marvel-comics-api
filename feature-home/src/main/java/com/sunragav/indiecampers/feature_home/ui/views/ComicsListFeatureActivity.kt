package com.sunragav.indiecampers.feature_home.ui.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.presentation.factory.ComicsViewModelFactory
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM
import com.sunragav.indiecampers.utils.ConnectivityState
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_comics_list_feature_land.*
import javax.inject.Inject

class ComicsListFeatureActivity : AppCompatActivity() {
    @Inject
    lateinit var connectivityState: ConnectivityState

    @Inject
    lateinit var networkStateRelay: NetworkStateRelay

    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory

    @Inject
    lateinit var viewModel: HomeVM

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

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeVM::class.java)


        val isTablet = resources.getBoolean(R.bool.isTablet)

        when {
            isTablet -> {
                setContentView(R.layout.content_comics_list_feature_land)
                val navHostFragment = navFragment as NavHostFragment
                if (viewModel.currentComics.value?.id == "default")
                    navHostFragment.navController.navigate(R.id.emptyFragment)

                viewModel.currentComics.observe(this, Observer<ComicsEntity> {
                    if (it.id != "default") {
                        navHostFragment.navController.navigate(R.id.comicsDetailFragment)
                    }
                })

            }
            else -> {

                setContentView(R.layout.activity_comics_list_feature)
            }
        }

        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    fun connected() {
        connectivityState.connected.getAndSet(true)
        networkStateRelay.relay.accept(NetworkState.CONNECTED)

    }

    fun disconnected() {
        connectivityState.connected.getAndSet(false)
        networkStateRelay.relay.accept(NetworkState.DISCONNECTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
