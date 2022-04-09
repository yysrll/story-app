package com.yusril.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.vo.Resource

interface IRepository {
    fun register(name: String, email: String, password: String) : LiveData<Resource<ResultResponse>>

    fun login(email: String, password: String) : LiveData<Resource<LoginResult>>
}