package com.rowicka.newthings.contacts.model

import android.net.Uri
import java.io.Serializable

data class ContactsInfo(
    val id: String = "",
    val name: String = "",
    var phone: String = "",
    val photoUri: Uri? = null
): Serializable