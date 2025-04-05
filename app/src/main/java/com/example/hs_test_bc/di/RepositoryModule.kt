package com.example.hs_test_bc.di

import com.example.hs_test_bc.data.repositoryImpl.GitHubRepositoryImpl
import com.example.hs_test_bc.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideGitHubRepository(
        gitHubRepositoryImpl: GitHubRepositoryImpl
    ): GitHubRepository
}