package com.rowicka.newthings.contacts

import android.Manifest
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import com.rowicka.newthings.R
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.databinding.ActivityContactsBinding
import com.rowicka.newthings.utils.checkPermission
import com.rowicka.newthings.utils.toast


class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding
    private val viewModel by viewModels<ContactsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

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

        queryAllContacts { cursor ->
            while (cursor.moveToNext()) {

                val hasPhoneNumber =
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
                if (hasPhoneNumber > 0) {
                    val contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoUri =
                        cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                    queryPhoneForContact(contactId) { phoneCursor ->
                        if (phoneCursor.moveToNext()) {
                            val phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            contactsInfoList.add(
                                ContactsInfo(
                                    contactId,
                                    displayName,
                                    phoneNumber,
                                    photoUri?.let { Uri.parse(photoUri) }
                                )
                            )
                        }
                    }
                }
            }
        }

        viewModel.updateList(contactsInfoList)
    }

    private fun queryAllContacts(actionOnCursor: (Cursor) -> Unit) {
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        ).use {
            it?.let { cursor -> actionOnCursor.invoke(cursor) }
        }
    }

    private fun queryPhoneForContact(contactId: String, actionOnCursor: (Cursor) -> Unit) {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        ).use {
            it?.let { phoneCursor -> actionOnCursor.invoke((phoneCursor)) }
        }
    }
}

fun String.phoneToColor(): Int {

    var phone = when {
        this.length > 7 -> this.substring(1, 8)
        this.length == 7 -> this.substring(1, 7) + "77"
        this.length == 6 -> this.substring(1, 6) + "666"
        else -> "9999999"
    }

    if (phone.startsWith("0")) {
        phone = phone.replaceFirst("0", "1")
    }

    phone = phone
        .replace(" ", "0")
        .replace("#", "0")
        .replace("*", "0")

    var color = "#${Integer.toString(phone.toInt(), 16)}"

    if (color.length == 7) {
        return Color.parseColor(color)
    }

    while (color.length != 7) {
        color += "0"
    }

    return Color.parseColor(color)
}

fun String.getInitialsFromName(): String {
    val tabName = this.split(" ")
    if (tabName.isNotEmpty()) {
        return tabName.map { it[0] }
            .joinToString("")
    }

    return ""
}
