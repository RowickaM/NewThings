package com.rowicka.newthings.contacts.ui.register

import android.os.Bundle
import android.provider.CallLog
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import com.rowicka.newthings.contacts.model.CallRegister
import com.rowicka.newthings.databinding.FragmentRegisterCallBinding
import java.util.*

class RegisterCallFragment : Fragment() {
    private lateinit var binding: FragmentRegisterCallBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterCallBinding.inflate(layoutInflater)
        binding.contactsCallLog.movementMethod = ScrollingMovementMethod()
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        showRegister()
    }

    private fun showRegister() {
        val sb = StringBuffer()
        this.requireActivity().contentResolver.query(CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            null).use {
            it?.let { managedCursor ->
                val numberColumn: Int = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
                val nameColumn: Int = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val typeColumn: Int = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
                val dateColumn: Int = managedCursor.getColumnIndex(CallLog.Calls.DATE)
                val durationColumn: Int = managedCursor.getColumnIndex(CallLog.Calls.DURATION)

                while (managedCursor.moveToNext()) {
                    val name = managedCursor.getStringOrNull(nameColumn)
                    val phoneNumber = managedCursor.getString(numberColumn)
                    val callDayTime = Date(managedCursor.getString(dateColumn).toLong())
                    val callDuration = managedCursor.getString(durationColumn)

                    val callType = when (managedCursor.getString(typeColumn).toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                        CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                        CallLog.Calls.MISSED_TYPE -> "MISSED"
                        else -> "unknown"
                    }
                    val callRegister = CallRegister(
                        name = name ?: "unknown",
                        phoneNumber = phoneNumber,
                        callType = callType,
                        callDate = callDayTime,
                        callDuration = callDuration,
                    )
                    sb.append(callRegister)
                    sb.append("\n----------------------------------\n")
                }
            }
        }

        binding.contactsCallLog.text = sb
    }
}