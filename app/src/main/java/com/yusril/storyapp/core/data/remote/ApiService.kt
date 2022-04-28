package com.yusril.storyapp.core.data.remote

import com.yusril.storyapp.core.data.remote.response.LoginResponse
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import com.yusril.storyapp.core.data.remote.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService
{
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
    ) : Call<ResultResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
    ) : Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
    ): Call<StoriesResponse>
}