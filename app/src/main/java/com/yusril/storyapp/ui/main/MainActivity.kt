package com.yusril.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusril.storyapp.R
import com.yusril.storyapp.core.data.local.UserPreferences
import com.yusril.storyapp.core.domain.model.Story
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.presentation.StoriesAdapter
import com.yusril.storyapp.core.presentation.ViewModelFactory
import com.yusril.storyapp.core.vo.Status
import com.yusril.storyapp.databinding.ActivityMainBinding
import com.yusril.storyapp.ui.auth.AuthViewModel
import com.yusril.storyapp.ui.auth.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var storiesViewModel: StoriesViewModel
    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initViewModel()

        val user = intent.getParcelableExtra<User>(USER)
        user?.token?.let { token ->
            storiesViewModel.getStories(token).observe(this){
                when(it.status) {
                    Status.LOADING -> {
                        Toast.makeText(this@MainActivity,
                            "loading", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        it.data?.let { stories ->
                            storiesAdapter.setStories(stories)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@MainActivity,
                            "Failure: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        storiesAdapter = StoriesAdapter()
        storiesAdapter.notifyDataSetChanged()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storiesAdapter
            setHasFixedSize(true)
        }
        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback{
            override fun onItemClicked(story: Story) {
                Toast.makeText(this@MainActivity, story.name, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(UserPreferences.getInstance(dataStore))
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        storiesViewModel = ViewModelProvider(this, factory)[StoriesViewModel::class.java]
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