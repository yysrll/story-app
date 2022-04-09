package com.yusril.storyapp.core.data

import androidx.lifecycle.LiveData
import com.yusril.storyapp.core.data.remote.RemoteDataSource
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.repository.IRepository
import com.yusril.storyapp.core.vo.Resource

class DataRepository private constructor(
    private val remoteDataSource: RemoteDataSource
) : IRepository {

    override fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Resource<ResultResponse>> = remoteDataSource.register(name, email, password)


    override fun login(email: String, password: String): LiveData<Resource<LoginResult>> = remoteDataSource.login(email, password)


    companion object {
        @Volatile
        private var instance : DataRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource
        ) : DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(remoteDataSource).apply {
                    instance = this
                }
            }
    }
}