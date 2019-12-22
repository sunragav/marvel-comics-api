package com.sunragav.indiecampers.feature_home.di

import android.app.Application
import com.sunragav.indiecampers.marvelcomics.application.MarvelComicsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DomainModule::class,
        DataModule::class,
        LocalPersistenceModule::class,
        RemoteModule::class,
        PresentationModule::class,
        AppModule::class
    ]
)
interface MarvelComicsAppComponent : AndroidInjector<MarvelComicsApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): MarvelComicsAppComponent
    }

    override fun inject(app: MarvelComicsApplication)
}