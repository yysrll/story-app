package com.yusril.storyapp.core.data.remote

import com.yusril.storyapp.core.data.remote.response.LoginResponse
import com.yusril.storyapp.core.data.remote.response.ResultResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService
{
    @FormUrlEncoded
    @POST("/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
    ) : Call<ResultResponse>

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
    ) : Call<LoginResponse>
}