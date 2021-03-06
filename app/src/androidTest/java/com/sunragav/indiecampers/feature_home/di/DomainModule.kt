package com.sunragav.indiecampers.feature_home.di

import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.qualifiers.Foreground
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Module
class DomainModule {
    @Singleton
    @Provides
    @Background
    fun providesBackgroundScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Singleton
    @Provides
    @Foreground
    fun providesForegroundScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Singleton
    @Provides
    fun provideCompoisteDisposable() = CompositeDisposable()
}