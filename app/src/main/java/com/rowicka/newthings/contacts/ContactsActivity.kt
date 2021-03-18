package com.rowicka.newthings.contacts

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
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
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()
        initObserver()
        requestPermission()
    }

    private fun initList(){
        adapter = ContactsAdapter()
        binding.contactsRecycleView.adapter = adapter
    }

    private fun initObserver(){
        viewModel.contactsList.observe(this){
            adapter.setList(it)
        }
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
                                    photoUri?.let{ Uri.parse(photoUri) }
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
