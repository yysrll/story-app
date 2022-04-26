package com.yusril.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.vo.Resource

interface IRepository {
    fun register(name: String, email: String, password: String) : LiveData<Resource<ResultResponse>>
    fun login(email: String, password: String) : LiveData<Resource<User>>

    fun getCurrentUser() : LiveData<User>
    suspend fun setNewUser(user: User)
    suspend fun deleteUser()

    fun getOnBoardingKey() : LiveData<Boolean>
    suspend fun setOnBoardingKey(state: Boolean)
}