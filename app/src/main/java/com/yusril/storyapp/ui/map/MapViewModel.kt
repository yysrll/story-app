package com.yusril.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.yusril.storyapp.core.domain.usecase.StoryUseCase

class MapViewModel(private val storyUseCase: StoryUseCase): ViewModel() {

    fun getStories(token: String) = storyUseCase.getStories(token)

}