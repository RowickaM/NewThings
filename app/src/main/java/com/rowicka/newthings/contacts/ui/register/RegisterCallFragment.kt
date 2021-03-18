package com.rowicka.newthings.contacts.ui.register

import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rowicka.newthings.databinding.FragmentRegisterCallBinding
import java.util.*


class RegisterCallFragment: Fragment() {
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

    private fun showRegister(){
        val sb = StringBuffer()
        this.requireActivity().contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null).use {
            it?.let{ managedCursor ->
                val number: Int = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
                val name: Int = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val type: Int = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
                val date: Int = managedCursor.getColumnIndex(CallLog.Calls.DATE)
                val duration: Int = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
                sb.append("Call Details :")
                while (managedCursor.moveToNext()) {
                    val phName: String = managedCursor.getString(name)
                    val phNumber: String = managedCursor.getString(number)
                    val callType: String = managedCursor.getString(type)
                    val callDate: String = managedCursor.getString(date)
                    val callDayTime = Date(callDate.toLong())
                    val callDuration: String = managedCursor.getString(duration)
                    var dir: String? = null
                    when (callType.toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                        CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                        CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                    }
                    sb.append("\nName:--- $phName \nPhone Number:--- $phNumber \nCall Type:--- $dir \nCall Date:--- $callDayTime \nCall duration in sec :--- $callDuration")
                    sb.append("\n----------------------------------")
                }
            }
        }

        binding.contactsCallLog.text = sb
    }
}