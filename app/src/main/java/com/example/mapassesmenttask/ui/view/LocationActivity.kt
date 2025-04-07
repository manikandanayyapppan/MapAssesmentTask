package com.example.mapassesmenttask.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mapassesmenttask.databinding.ActivityLocationBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding: ActivityLocationBinding? = null
    private var currentLocation: Location? = null
    private var currentMarker: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding?.uiMapView?.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@LocationActivity)
        }
        showLocationSettingsDialog()
        setUpListeners()
        binding?.uiIvBackButton?.setOnClickListener {
            finish()
        }
    }

    private fun updateMarkerAddress(latLng: LatLng) {
        val address = findAddressFromLatLng(latLng.latitude, latLng.longitude)
        Log.d("LocationActivity", "Marker moved to: $latLng, Address: $address")
        updateEditText(address)
    }

    private fun findAddressFromLatLng(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
            address?.getAddressLine(0) ?: ""
        } catch (e: Exception) {
            Toast.makeText(this, "Address cannot be detected", Toast.LENGTH_SHORT).show()
            ""
        }
    }

    private fun updateEditText(address: String) {
        binding?.uiEtBusinessLocation?.setText(address)
    }

    private fun setUpListeners() {
        binding?.uiBtnConfirmLocation?.setOnClickListener {
           confirmLocation()

        }
        binding?.uiCvUseCurrentLocation?.setOnClickListener {
            showLocationSettingsDialog()
        }
    }

    private fun confirmLocation() {
        val resultIntent = Intent().apply {
            putExtra("CONFIRM_LOCATION", "${binding?.uiEtBusinessLocation?.text}")
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("locLog", "lastLocation $location")
                if(location != null) {
                    location.let {
                        currentMarker?.remove()
                        currentLocation = it
                        binding?.uiMapView?.getMapAsync(this)
                        updateMarkerAddress(it.toLatLng())
                    }
                } else {
                    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                        .setWaitForAccurateLocation(false)
                        .setMinUpdateIntervalMillis(1000)
                        .setMaxUpdateDelayMillis(5000)
                        .build()

                    fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            fusedLocationClient.removeLocationUpdates(this)
                            location?.let {
                                currentMarker?.remove()
                                currentLocation = location
//                                updateMarkerAddress(location.toLatLng())
                            } ?: {
                                Log.e("locLog", "Real-time location is null")
                                fetchLocation()
                            }
                        }
                    }, Looper.getMainLooper())
                }
            }
            .addOnFailureListener { e ->
                Log.e("locLog", "Failed to get location: ${e.message}")
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0)
        val markerOptions = MarkerOptions().position(latLng)

        currentMarker = googleMap.addMarker(markerOptions)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        googleMap.setOnMapClickListener { location ->
            currentMarker?.remove()
            currentMarker = googleMap.addMarker(
                markerOptions.position(location)
            )
            updateMarkerAddress(location)
        }
    }

    private fun showLocationSettingsDialog() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000)
            .setMaxUpdateDelayMillis(5000)
            .build()

        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()

        LocationServices.getSettingsClient(this)
            .checkLocationSettings(settingsRequest)
            .addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)
                    Log.d("TAG", "Location settings are satisfied.")
                    fetchLocation()
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                (e as ResolvableApiException).startResolutionForResult(this, 200)
                            } catch (sendEx: IntentSender.SendIntentException) {
                                Log.e("TAG", "Error resolving location settings: ${sendEx.message}")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            Log.e("TAG", "Location settings cannot be changed to meet requirements.")
                            Toast.makeText(
                                this,
                                "Location settings are not available on this device.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Log.e("TAG", "Unexpected status code: ${e.statusCode}")
                        }
                    }
                }
            }
    }
}

fun Location.toLatLng(): LatLng = LatLng(latitude, longitude)