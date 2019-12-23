package com.sunragav.indiecampers.feature_home.di

import com.sunragav.indiecampers.home.data.repository.RemoteRepository
import com.sunragav.indiecampers.marvelcomics.BuildConfig
import com.sunragav.indiecampers.feature_home.utils.FakeComicsService
import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.datasource.NetworkDataSource
import com.sunragav.indiecampers.remotedata.http.ApiKeyInterceptor
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import com.sunragav.indiecampers.remotedata.qualifiers.PrivateKey
import com.sunragav.indiecampers.remotedata.qualifiers.PublicKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [RemoteModule.Binders::class])
class RemoteModule {

    @Module
    interface Binders {

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: NetworkDataSource
        ): RemoteRepository

    }
    @Provides
    @PublicKey
    fun providePublicKey() = BuildConfig.PUBLIC_KEY

    @Provides
    @PrivateKey
    fun providePrivateKey() = BuildConfig.PRIVATE_KEY
    @Provides
    @Singleton
    fun provideComicsRemoteMapper() = ComicsRemoteMapper()

    @Provides
    @Singleton
    fun provideComicsService(retrofit: Retrofit): ComicsService =
        FakeComicsService()


    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val level = getInterceptorLevel()
        httpLoggingInterceptor.level = level
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .cache(null)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()
    }

    private fun getInterceptorLevel(): HttpLoggingInterceptor.Level? {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

}