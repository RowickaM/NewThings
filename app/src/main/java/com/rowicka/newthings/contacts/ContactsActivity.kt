package com.rowicka.newthings.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

    private fun requestPermission() {
        checkPermission(
            Manifest.permission.READ_CONTACTS,
            onGranted = { getContacts() },
            onDenied = { permissionDenied() },
            onNeverAskAgain = { permissionDenied() }
        )

    }

    private fun permissionDenied() {
        toast(R.string.contacts_need_permission)
    }

    private fun getContacts() {

        val contactsInfoList = arrayListOf<ContactsInfo>()

        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        ).use {
            it?.let { cursor ->

                Log.d("MRMRMR", "ContactsActivity.kt: $cursor")
                while (it.moveToNext()) {
                    val hasPhoneNumber =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
                    if (hasPhoneNumber > 0) {

                        val contactsInfo = ContactsInfo()
                        val contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val displayName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                        contactsInfo.id = contactId
                        contactsInfo.name = displayName

                        contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        ).use {
                            it?.let { phoneCursor ->
                                if (phoneCursor.moveToNext()) {
                                    val phoneNumber =
                                        phoneCursor.getString(phoneCursor.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER))

                                    contactsInfo.phone = phoneNumber
                                }

                                phoneCursor.close()

                                contactsInfoList.add(contactsInfo)
                            }
                        }


                    }
                }
            }

        }


        Log.d("MRMRMR", "ContactsActivity.kt: $contactsInfoList")
    }
}


data class ContactsInfo(var id: String = "", var name: String = "", var phone: String = "")

fun ComponentActivity.checkPermission(
    permission: String,
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {},
    onNeverAskAgain: () -> Unit = {},
) {
    val permissionRequest = activityResultRegistry.register(
        "REQUEST PERMISSION $permission",
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            onDenied()
        }
    }

    val permissionState = ContextCompat.checkSelfPermission(this, permission)
    val showRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.shouldShowRequestPermissionRationale(permission)
    } else {
        true
    }

    when {
        permissionState == PackageManager.PERMISSION_GRANTED -> onGranted.invoke()
        !showRationale -> onNeverAskAgain.invoke()
        else -> permissionRequest.launch(permission)
    }
}
