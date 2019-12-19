package com.sunragav.indiecampers.marvelcomics.di

import android.app.Application
import android.content.Context
import com.sunragav.indiecampers.feature_home.ui.views.ComicsDetailFragment
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivity
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivityFragment
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
    internal abstract fun contributesComicsListFeautreFragment(): ComicsListFeatureActivityFragment

    @ContributesAndroidInjector
    internal abstract fun contributesComicsDetailsFeautreFragment(): ComicsDetailFragment

}