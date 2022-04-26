package com.yusril.storyapp.core.utils

import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.domain.model.User

object DataMapper {
    fun mapLoginResultToUser(input: LoginResult) =
        User(
            id = input.userId,
            name = input.name,
            token = input.token
        )
}