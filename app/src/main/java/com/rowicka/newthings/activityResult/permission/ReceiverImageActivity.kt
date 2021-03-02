package com.rowicka.newthings.activityResult.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rowicka.newthings.R
import com.rowicka.newthings.databinding.ActivityReceiverImageBinding
import com.rowicka.newthings.utils.invisible
import com.rowicka.newthings.utils.show
import com.rowicka.newthings.utils.toast

class ReceiverImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiverImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReceiverImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.receiverHeader.invisible()
        initListeners()
    }

    //region result
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri -> getImage(uri) }
            }
        }

    private fun getImage(imageUri: Uri) {
        binding.apply {
            receiverHeader.show()
            receiverImage.setImageURI(imageUri)
        }
    }
    // endregion

    //region permission
    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                goToGallery()
            } else {
                permissionGalleryDenied()
            }
        }

    private fun navigateToGallery() {
        when (ContextCompat.checkSelfPermission(this, permission)) {
            PackageManager.PERMISSION_GRANTED -> goToGallery()
            else -> requestGalleryPermission.launch(permission)
        }
    }

    private fun goToGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK

        galleryResult.launch(intent)
    }

    private fun permissionGalleryDenied() {
        toast(R.string.activity_result_error_permission)
    }
    //endregion

    private fun initListeners() {
        binding.receiverGetImageButton.setOnClickListener { navigateToGallery() }
    }
}