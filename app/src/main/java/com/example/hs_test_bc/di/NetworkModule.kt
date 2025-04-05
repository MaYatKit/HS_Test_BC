package com.example.hs_test_bc.di

import android.content.Context
import com.example.hs_test_bc.BuildConfig
import com.example.hs_test_bc.data.remote.api.AuthService
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
import javax.inject.Qualifier
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
        okClientBuilder.addNetworkInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        okClientBuilder.retryOnConnectionFailure(false)
        return okClientBuilder.build()
    }

    @GitHubRetrofit
    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .client(okHttp)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @AuthRetrofit
    @Singleton
    @Provides
    fun provideAuthRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideGitHubApiService(@GitHubRetrofit retrofit: Retrofit): GitHubApi =
        retrofit.create(GitHubApi::class.java)

    @Singleton
    @Provides
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GitHubRetrofit