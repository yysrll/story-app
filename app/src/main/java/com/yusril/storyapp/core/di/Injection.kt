package com.yusril.storyapp.core.di

import com.yusril.storyapp.core.data.DataRepository
import com.yusril.storyapp.core.data.remote.ApiConfig
import com.yusril.storyapp.core.data.remote.RemoteDataSource
import com.yusril.storyapp.core.domain.repository.IRepository
import com.yusril.storyapp.core.domain.usecase.StoryInteractor
import com.yusril.storyapp.core.domain.usecase.StoryUseCase

object Injection {
    private fun provideRepository() : IRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.getApiService())

        return DataRepository.getInstance(remoteDataSource)
    }

    fun provideUseCase() : StoryUseCase {
        val repository = provideRepository()

        return StoryInteractor(repository)
    }
}