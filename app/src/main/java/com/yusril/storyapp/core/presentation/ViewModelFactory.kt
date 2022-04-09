package com.yusril.storyapp.core.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yusril.storyapp.core.di.Injection
import com.yusril.storyapp.core.domain.usecase.StoryUseCase
import com.yusril.storyapp.ui.auth.AuthViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val storyUseCase: StoryUseCase
    ) : ViewModelProvider.NewInstanceFactory()
{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(storyUseCase) as T
            else -> throw Throwable("Unknown viewModel class : " +modelClass.name)
        }


    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUseCase(context))
            }
    }
}