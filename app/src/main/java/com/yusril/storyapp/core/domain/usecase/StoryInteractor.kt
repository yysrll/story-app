package com.yusril.storyapp.core.domain.usecase

import androidx.lifecycle.LiveData
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.repository.IRepository
import com.yusril.storyapp.core.vo.Resource

class StoryInteractor(private val repository: IRepository) : StoryUseCase {
    override fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Resource<ResultResponse>> = repository.register(name, email, password)

    override fun login(email: String, password: String): LiveData<Resource<LoginResult>> = repository.login(email, password)
}