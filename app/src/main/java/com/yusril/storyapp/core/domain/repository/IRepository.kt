package com.yusril.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.yusril.storyapp.core.data.local.room.StoryItem
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.domain.model.Story
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.vo.Resource
import java.io.File

interface IRepository {
    fun register(name: String, email: String, password: String) : LiveData<Resource<ResultResponse>>
    fun login(email: String, password: String) : LiveData<Resource<User>>

    fun getCurrentUser() : LiveData<User>
    suspend fun setNewUser(user: User)
    suspend fun deleteUser()

    fun getOnBoardingKey() : LiveData<Boolean>
    suspend fun setOnBoardingKey(state: Boolean)

    fun getStories(token: String) : LiveData<Resource<List<Story>>>
    fun getStoriesWithPaging(token: String) : LiveData<PagingData<StoryItem>>
    fun uploadStory(token: String, file: File, description: String, lat: Float?, lon: Float?) : LiveData<Resource<ResultResponse>>
}