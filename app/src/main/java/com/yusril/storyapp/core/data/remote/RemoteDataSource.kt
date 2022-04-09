package com.yusril.storyapp.core.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yusril.storyapp.core.data.remote.response.LoginResponse
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.vo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(
    private val apiService: ApiService
){

    fun register(name: String, username: String, password: String) : LiveData<Resource<ResultResponse>>
    {
        val registerResult = MutableLiveData<Resource<ResultResponse>>()
        apiService.register(name, username, password).enqueue(object : Callback<ResultResponse>
        {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                registerResult.value = Resource.loading()
                if (response.isSuccessful) {
                    registerResult.value = Resource.success(response.body())
                } else {
                    registerResult.value = Resource.error(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                registerResult.value = Resource.error(t.message)
            }

        })
        return registerResult
    }


    fun login(username: String, password: String) : LiveData<Resource<LoginResult>>
    {
        val loginResult = MutableLiveData<Resource<LoginResult>>()
        apiService.login(username, password).enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                loginResult.value = Resource.loading()
                if (response.isSuccessful) {
                    loginResult.value = Resource.success(response.body()?.loginResult)
                } else {
                    loginResult.value = Resource.error(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = Resource.error(t.message)
            }

        })
        return loginResult
    }


    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService) : RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }
}