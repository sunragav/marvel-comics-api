package com.sunragav.indiecampers.feature_home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.presentation.factory.ComicsViewModelFactory
import com.sunragav.indiecampers.home.presentation.mapper.ComicsEntityMapper
import com.sunragav.indiecampers.home.presentation.models.Comics
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM
import com.sunragav.indiecampers.feature_home.di.qualifiers.ViewModelKey
import com.sunragav.indiecampers.utils.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [PresentationModule.Binders::class])
class PresentationModule {
    @Module
    interface Binders {

        @Binds
        fun bindsViewModelFactory(
            factory: ComicsViewModelFactory
        ): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(HomeVM::class)
        fun bindsHomeViewModel(homeVM: HomeVM): ViewModel
    }


    @Provides
    @Singleton
    fun provideComicsEntityMapper(): Mapper<ComicsEntity, Comics> = ComicsEntityMapper()
}