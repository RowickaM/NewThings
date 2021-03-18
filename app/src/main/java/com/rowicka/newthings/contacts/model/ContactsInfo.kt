package com.rowicka.newthings.contacts.model

import android.net.Uri

data class ContactsInfo(
    val id: String = "",
    val name: String = "",
    var phone: String = "",
    val photoUri: Uri? = null
)