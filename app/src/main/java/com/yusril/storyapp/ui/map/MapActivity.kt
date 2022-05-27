package com.yusril.storyapp.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yusril.storyapp.R
import com.yusril.storyapp.core.data.local.UserPreferences
import com.yusril.storyapp.core.domain.model.Story
import com.yusril.storyapp.core.domain.model.User
import com.yusril.storyapp.core.presentation.ViewModelFactory
import com.yusril.storyapp.core.vo.Status.*
import com.yusril.storyapp.databinding.ActivityMapBinding
import com.yusril.storyapp.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var user: User
    private var _stories = MutableLiveData<List<Story>?>()
    private var stories: LiveData<List<Story>?> = _stories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
        user = intent.getParcelableExtra<User>(MainActivity.USER) as User
        viewModel.getStories(user.token).observe(this){
            when(it.status){
                SUCCESS -> _stories.postValue(it.data)
                ERROR -> {}
                LOADING -> {}
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled  = true
        }


        stories.observe(this) {
            it?.map { story ->
                val lat = story.lat.toDouble()
                val lon = story.lon.toDouble()

                val loc = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions()
                    .position(loc)
                    .title(story.name)
                    .snippet(story.description)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
            }
        }

        getMyLocation()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) getMyLocation()
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this, UserPreferences.getInstance(dataStore))
        viewModel = ViewModelProvider(this, factory)[MapViewModel::class.java]
    }

    companion object {
        private const val USER = "user"
        fun start(context: Activity, user: User) {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra(USER, user)
            context.startActivity(intent)
        }
    }
}