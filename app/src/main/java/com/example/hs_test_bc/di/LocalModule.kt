package com.example.hs_test_bc.di

import com.example.hs_test_bc.data.local.AuthPreferences
import com.example.hs_test_bc.data.repositoryImpl.GitHubRepositoryImpl
import com.example.hs_test_bc.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

//    @Binds
//    @Singleton
//    abstract fun provideAuthPreferences(
//        authPreferences: AuthPreferences
//    ): AuthPreferences
}