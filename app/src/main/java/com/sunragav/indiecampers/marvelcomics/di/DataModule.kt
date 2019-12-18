package com.sunragav.indiecampers.marvelcomics.di

import com.sunragav.indiecampers.home.data.repository.ComicsDataRepositoryImpl
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun bindsRepository(
        repoImpl: ComicsDataRepositoryImpl
    ): ComicsDataRepository

}