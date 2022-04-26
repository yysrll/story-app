package com.yusril.storyapp.core.di

import com.yusril.storyapp.core.data.DataRepository
import com.yusril.storyapp.core.data.local.LocalDataSource
import com.yusril.storyapp.core.data.local.UserPreferences
import com.yusril.storyapp.core.data.remote.ApiConfig
import com.yusril.storyapp.core.data.remote.RemoteDataSource
import com.yusril.storyapp.core.domain.repository.IRepository
import com.yusril.storyapp.core.domain.usecase.StoryInteractor
import com.yusril.storyapp.core.domain.usecase.StoryUseCase

object Injection {
    private fun provideRepository(preferences: UserPreferences) : IRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.getApiService())
        val localDataSource = LocalDataSource.getInstance(preferences)

        return DataRepository.getInstance(remoteDataSource, localDataSource)
    }

    fun provideUseCase(preferences: UserPreferences) : StoryUseCase {
        val repository = provideRepository(preferences)

        return StoryInteractor(repository)
    }
}