package com.rowicka.newthings.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rowicka.newthings.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}