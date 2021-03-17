package com.rowicka.newthings.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.rowicka.newthings.R
import com.rowicka.newthings.databinding.ActivityContactsBinding
import com.rowicka.newthings.utils.toast

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }

    private fun requestPermission(){
        checkPermission(
            Manifest.permission.READ_CONTACTS,
            onGranted = { getContacts() },
            onDenied = { permissionDenied() },
            onNeverAskAgain = { permissionDenied() }
        )

    }

    private fun getContacts(){}

    private fun permissionDenied(){
        toast(R.string.contacts_need_permission)
    }
}

fun ComponentActivity.checkPermission(
    permission: String,
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {},
    onNeverAskAgain: () -> Unit = {}
){
    val permissionRequest = activityResultRegistry.register(
        "REQUEST PERMISSION $permission",
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if (isGranted){
            onGranted()
        }else{
            onDenied()
        }
    }

    val permissionState = ContextCompat.checkSelfPermission(this, permission)
    val showRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.shouldShowRequestPermissionRationale(permission)
    } else {
        true
    }

    when{
        permissionState == PackageManager.PERMISSION_GRANTED -> onGranted.invoke()
        !showRationale -> onNeverAskAgain.invoke()
        else -> permissionRequest.launch(permission)
    }
}
