package com.yusril.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.yusril.storyapp.core.domain.usecase.StoryUseCase

class StoriesViewModel(private val storyUseCase: StoryUseCase): ViewModel() {
    fun getStories(token: String) = storyUseCase.getStories(token)
}