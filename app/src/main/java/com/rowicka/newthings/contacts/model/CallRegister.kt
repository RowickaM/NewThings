package com.rowicka.newthings.contacts.model

import java.util.*

data class CallRegister(
    val name: String? = null,
    val phoneNumber: String,
    val callType: String?,
    val callDate: Date,
    val callDuration: String,
)
