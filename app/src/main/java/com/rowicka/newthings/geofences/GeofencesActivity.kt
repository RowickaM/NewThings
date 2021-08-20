package com.rowicka.newthings.geofences

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.rowicka.newthings.BuildConfig
import com.rowicka.newthings.R
import com.rowicka.newthings.databinding.ActivityGeofencesBinding
import com.rowicka.newthings.utils.toast

class GeofencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeofencesBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var mViewModel: GeofenceViewModel

    companion object {

        internal const val ACTION_GEOFENCE_EVENT =
            "GeofencesActivity.treasureHunt.action.ACTION_GEOFENCE_EVENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(
            this, SavedStateViewModelFactory(
                this.application,
                this
            )
        ).get(GeofenceViewModel::class.java)

        binding = ActivityGeofencesBinding.inflate(layoutInflater).apply {
            viewmodel = mViewModel
            lifecycleOwner = this@GeofencesActivity
        }
        setContentView(binding.root)

        geofencingClient = LocationServices.getGeofencingClient(this)

        // Create channel for notifications
        createChannel(this)
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartGeofencing()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeGeofences()
    }

    //region permission
    private fun checkPermissionsAndStartGeofencing() {
        foregroundLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val foregroundLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            if (!checkBackgroundLocation()) {
                backgroundLocationPermission.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                onGrantedPermissions()
            }
        } else {
            onDeniedPermissions()
        }
    }

    private fun checkForegroundLocation(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val backgroundLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            onGrantedPermissions()
        }
    }

    private fun checkBackgroundLocation(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        } else {
            return true
        }
    }

    private fun onDeniedPermissions() {
        Snackbar.make(
            binding.activityMapsMain,
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }

    private fun onGrantedPermissions() {
        checkDeviceLocationSettingsAndStartGeofence()
    }
    //endregion permission

    //region location
    private fun checkDeviceLocationSettingsAndStartGeofence(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { onDisabledLocation(it, resolve) }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                addGeofencesClue()
            }
        }
    }

    private fun onDisabledLocation(exception: Exception, resolve: Boolean) {
        if (exception is ResolvableApiException && resolve) {
            try {
                exception.startResolutionForResult(
                    this@GeofencesActivity,
                    REQUEST_TURN_DEVICE_LOCATION_ON
                )
            } catch (sendEx: IntentSender.SendIntentException) {
                Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
            }
        } else {
            Snackbar.make(
                binding.activityMapsMain,
                R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
            ).setAction(android.R.string.ok) {
                checkDeviceLocationSettingsAndStartGeofence()
            }.show()
        }
    }
    // endregion location

    private val geofencePendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
            .apply {
                action = ACTION_GEOFENCE_EVENT
            }
        PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("MissingPermission")
    private fun addGeofencesClue() {
        if (mViewModel.geofenceIsActive()) return
        val currentGeofenceIndex = mViewModel.nextGeofenceIndex()
        if (currentGeofenceIndex >= GeofencingConstants.NUM_LANDMARKS) {
            removeGeofences()
            mViewModel.geofenceActivated()
            return
        }
        val currentGeofenceData = GeofencingConstants.LANDMARK_DATA[currentGeofenceIndex]

        val geofence = Geofence.Builder()
            .setRequestId(currentGeofenceData.id)
            .setCircularRegion(
                currentGeofenceData.latLong.latitude,
                currentGeofenceData.latLong.longitude,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            if (checkForegroundLocation()) {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        toast(R.string.geofences_added)
                        Log.d("Add Geofence", geofence.requestId)
                        mViewModel.geofenceActivated()
                    }
                    addOnFailureListener {
                        toast(R.string.geofences_not_added)
                        it.message?.let { message -> Log.w(TAG, message) }
                    }
                }
            }
        }
    }

    private fun removeGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d(TAG, getString(R.string.geofences_removed))
                toast(R.string.geofences_removed)
            }
            addOnFailureListener {
                Log.d(TAG, getString(R.string.geofences_not_removed))
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            if (extras.containsKey(GeofencingConstants.EXTRA_GEOFENCE_INDEX)) {
                mViewModel.updateHint(extras.getInt(GeofencingConstants.EXTRA_GEOFENCE_INDEX))
                checkPermissionsAndStartGeofencing()
            }
        }
    }

}

private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val TAG = "GeofencesActivity"
private const val LOCATION_PERMISSION_INDEX = 0
private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1