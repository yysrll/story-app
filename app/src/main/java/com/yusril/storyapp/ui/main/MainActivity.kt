package com.yusril.storyapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.yusril.storyapp.R
import com.yusril.storyapp.core.data.local.UserPreferences
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.presentation.ViewModelFactory
import com.yusril.storyapp.databinding.ActivityMainBinding
import com.yusril.storyapp.ui.auth.AuthViewModel
import com.yusril.storyapp.ui.auth.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        val user = intent.getParcelableExtra<User>(USER)
        binding.name.text = user?.name
    }

    private fun init() {
        val factory = ViewModelFactory.getInstance(UserPreferences.getInstance(dataStore))
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                authViewModel.deleteUser()
                LoginActivity.start(this)
                finish()
            }
        }
        return true
    }

    companion object {
        const val USER = "user"
        fun start(context: Activity, user: User) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(USER, user)
            context.startActivity(intent)
            context.finish()
        }
    }
}