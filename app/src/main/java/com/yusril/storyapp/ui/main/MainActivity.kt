package com.yusril.storyapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.yusril.storyapp.ui.createstory.CreateStoryActivity
import com.yusril.storyapp.ui.detail.DetailActivity

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
        showLoading(true)

        val user = intent.getParcelableExtra<User>(USER)
        binding.buttonAddStory.setOnClickListener {
            user?.let { user -> CreateStoryActivity.start(this, user) }
        }


        user?.token?.let { token ->
            storiesViewModel.getStories(token).observe(this){
                when(it.status) {
                    Status.LOADING -> {
                        showLoading(true)
                    }
                    Status.SUCCESS -> {
                        it.data?.let { stories ->
                            storiesAdapter.setStories(stories)
                        }
                        showLoading(false)
                    }
                    Status.ERROR -> {
                        showLoading(false)
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
                DetailActivity.start(this@MainActivity, story)
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
                showLogoutDialog()
            }
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return true
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            rvStory.visibility = if (show) View.GONE else View.VISIBLE
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            progressBar.apply {
                if (show) showShimmer(true) else stopShimmer()
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.logout_dialog_title))
        dialog.setPositiveButton(getString(R.string.logout_yes)) { _,_ ->
            authViewModel.deleteUser()
            LoginActivity.start(this)
            finish()
        }
        dialog.setNegativeButton(getString(R.string.cancel)) { _,_ ->
        }
        dialog.show()
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