package com.sunragav.indiecampers.feature_home.di

import android.app.Application
import android.content.Context
import com.sunragav.indiecampers.feature_home.ui.views.ComicsDetailFragment
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivity
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureFragment
import com.sunragav.indiecampers.marvelcomics.SplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): SplashActivity

    @ContributesAndroidInjector
    internal abstract fun contributesComicsListFeautreActivity(): ComicsListFeatureActivity

    @ContributesAndroidInjector
    internal abstract fun contributesComicsListFeautreFragment(): ComicsListFeatureFragment

    @ContributesAndroidInjector
    internal abstract fun contributesComicsDetailsFeautreFragment(): ComicsDetailFragment

}