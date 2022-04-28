package com.yusril.storyapp.core.utils

import com.yusril.storyapp.core.data.remote.response.LoginResult
import com.yusril.storyapp.core.data.remote.response.StoryResponse
import com.yusril.storyapp.core.domain.model.Story
import com.yusril.storyapp.core.domain.model.User

object DataMapper {
    fun mapLoginResultToUser(input: LoginResult) =
        User(
            id = input.userId,
            name = input.name,
            token = "Bearer ${input.token}"
        )

    fun mapStoryResponseToStory(input: List<StoryResponse>) : List<Story> {
        val listStory = ArrayList<Story>()
        input.map {
            val story = Story(
                id = it.id,
                name = it.name,
                description = it.description,
                photoUrl = it.photoUrl,
                createdAt = it.createdAt
            )
            listStory.add(story)
        }
        return listStory
    }

}