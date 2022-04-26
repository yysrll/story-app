package com.yusril.storyapp.core.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yusril.storyapp.core.data.remote.response.LoginResponse
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.utils.DataMapper
import com.yusril.storyapp.core.vo.Resource
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(
    private val apiService: ApiService
){

    fun register(name: String, username: String, password: String) : LiveData<Resource<ResultResponse>>
    {
        val registerResult = MutableLiveData<Resource<ResultResponse>>()
        registerResult.value = Resource.loading()
        apiService.register(name, username, password).enqueue(object : Callback<ResultResponse>
        {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                if (response.isSuccessful) {
                    registerResult.value = Resource.success(response.body())
                } else {
                    val errorMsg = JSONObject(response.errorBody()?.string()!!)
                    registerResult.value = Resource.error(errorMsg.getString("message"))
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                registerResult.value = Resource.error(t.message)
            }

        })
        return registerResult
    }


    fun login(username: String, password: String) : LiveData<Resource<User>>
    {
        val loginResult = MutableLiveData<Resource<User>>()
        loginResult.value = Resource.loading()
        apiService.login(username, password).enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    loginResult.value = Resource.success(response.body()?.loginResult?.let {
                        DataMapper.mapLoginResultToUser(
                            it
                        )
                    })
                } else {
                    val errorMsg = JSONObject(response.errorBody()?.string()!!)
                    loginResult.value = Resource.error(errorMsg.getString("message"))
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