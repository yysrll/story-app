package com.yusril.storyapp.core.data

import androidx.lifecycle.LiveData
import com.yusril.storyapp.core.data.local.LocalDataSource
import com.yusril.storyapp.core.data.remote.RemoteDataSource
import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.domain.repository.IRepository
import com.yusril.storyapp.core.vo.Resource

class DataRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {

    override fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Resource<ResultResponse>> = remoteDataSource.register(name, email, password)

    override fun login(email: String, password: String): LiveData<Resource<User>> = remoteDataSource.login(email, password)

    override fun getCurrentUser(): LiveData<User> = localDataSource.getCurrentUser()
    override suspend fun setNewUser(user: User) = localDataSource.setNewUser(user)
    override suspend fun deleteUser() = localDataSource.deleteUser()

    override fun getOnBoardingKey(): LiveData<Boolean> = localDataSource.getOnBoardingKey()
    override suspend fun setOnBoardingKey(state: Boolean) = localDataSource.setOnBoardingKey(state)


    companion object {
        @Volatile
        private var instance : DataRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ) : DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(remoteDataSource, localDataSource).apply {
                    instance = this
                }
            }
    }
}