package com.yusril.storyapp.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yusril.storyapp.R
import com.yusril.storyapp.core.presentation.ViewModelFactory
import com.yusril.storyapp.core.vo.Status
import com.yusril.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setMyButtonEnable()
        binding.btnRegister.setOnClickListener { RegisterActivity.start(this) }
        binding.btnLogin.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            login()
        }

        binding.inputEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing
            }

        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
                // Nothing
            }

        })


    }

    private fun login() {
        val email = binding.inputEmail.text?.trim().toString()
        val password = binding.inputPassword.text?.trim().toString()
        viewModel.login(email, password).observe(this){
            when(it.status) {
                Status.LOADING -> {
                    showLoading(true)
                    Log.d("LoginResult", "Loading")
                }
                Status.ERROR -> {
                    showLoading(false)
                    Toast.makeText(this, "error, ${it.message}", Toast.LENGTH_SHORT).show()
                    Log.d("LoginResult", "error, ${it.message}")
                }
                Status.SUCCESS -> {
                    showLoading(false)
                    Toast.makeText(this, "success, ${it.data?.name}", Toast.LENGTH_SHORT).show()
                    Log.d("LoginResult", "success")
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val emailResult = binding.inputEmail.text
        val passwordResult = binding.inputPassword.text
        val isValidEmail = emailResult != null && emailResult.toString().isNotEmpty()
        val isValidPassword = passwordResult != null && passwordResult.toString().isNotEmpty()
        val isNotError = binding.inputPassword.error == null && binding.inputEmail.error == null
        binding.btnLogin.isEnabled = isValidEmail && isValidPassword && isNotError
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.btnLogin.apply {
                text = ""
                isEnabled = false
            }
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnLogin.apply {
                text = resources.getString(R.string.login)
                isEnabled = true
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun init() {
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    companion object {
        fun start(context: Activity?) {
            val intent = Intent(context, LoginActivity::class.java)
            context?.startActivity(intent)
        }
    }
}