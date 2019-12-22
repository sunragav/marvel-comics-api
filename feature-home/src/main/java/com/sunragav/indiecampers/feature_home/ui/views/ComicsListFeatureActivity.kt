package com.sunragav.indiecampers.feature_home.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.sunragav.indiecampers.android_utils.ConnectivityMonitorLiveData
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.presentation.factory.ComicsViewModelFactory
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.content_comics_list_feature_land.*
import javax.inject.Inject

class ComicsListFeatureActivity : AppCompatActivity() {

    @Inject
    lateinit var networkStateRelay: NetworkStateRelay

    @Inject
    lateinit var connectivityState: ConnectivityMonitorLiveData

    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory

    @Inject
    lateinit var viewModel: HomeVM

    private var alreadyNavigatedToComicsDetailFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        networkStateRelay.relay.accept(NetworkState.EMPTY)
        connectivityState.observe(this, Observer {
            if (it == true) connected() else disconnected()
        })

        Glide.get(this).setMemoryCategory(MemoryCategory.LOW)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeVM::class.java)

        val isTablet = resources.getBoolean(R.bool.isTablet)

        when {
            isTablet -> {
                setContentView(R.layout.content_comics_list_feature_land)
                val navHostFragment = navFragment as NavHostFragment
                if (viewModel.currentComics.value?.id == "default")
                    navHostFragment.navController.navigate(R.id.emptyFragment)

                viewModel.currentComics.observe(this, Observer<ComicsEntity> {
                    if (it.id != "default" && alreadyNavigatedToComicsDetailFragment.not()) {
                        alreadyNavigatedToComicsDetailFragment = true
                        navHostFragment.navController.navigate(R.id.comicsDetailFragment)
                    }
                })

            }
            else -> {

                setContentView(R.layout.activity_comics_list_feature)
            }
        }


    }

    override fun onDestroy() {
        viewModel.currentComics.removeObservers(this)
        super.onDestroy()
    }

    private fun connected() {
        networkStateRelay.relay.accept(NetworkState.CONNECTED)

    }

    private fun disconnected() {
        networkStateRelay.relay.accept(NetworkState.DISCONNECTED)
    }
}
