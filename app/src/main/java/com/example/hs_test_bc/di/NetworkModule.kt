package com.example.hs_test_bc.di

import android.content.Context
import com.example.hs_test_bc.BuildConfig
import com.example.hs_test_bc.data.remote.api.GitHubApi
import com.example.hs_test_bc.utils.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val okClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
        okClientBuilder.retryOnConnectionFailure(false)
        return okClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .client(okHttp)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideGitHubApiService(retrofit: Retrofit): GitHubApi =
        retrofit.create(GitHubApi::class.java)


    @Singleton
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}