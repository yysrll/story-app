package com.yusril.storyapp.ui.auth

import androidx.lifecycle.ViewModel
import com.yusril.storyapp.core.domain.usecase.StoryUseCase

class AuthViewModel(private val storyUseCase: StoryUseCase): ViewModel() {
    fun register(name: String, email: String, password: String) = storyUseCase.register(name, email, password)

    fun login(email: String, password: String) = storyUseCase.login(email, password)
}