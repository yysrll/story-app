package com.yusril.storyapp.ui.onboarding

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.yusril.storyapp.R
import com.yusril.storyapp.databinding.ActivityOnBoardingBinding
import com.yusril.storyapp.ui.auth.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            LoginActivity.start(this)
        }
    }


    companion object {
        fun start(context : Activity?) {
            val intent = Intent(context, OnBoardingActivity::class.java)
            context?.startActivity(intent)
        }
    }
}