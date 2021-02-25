package com.rowicka.newthings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rowicka.newthings.activityResult.ReceiverActivity
import com.rowicka.newthings.databinding.ActivityMainBinding
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners(){
        binding.apply {
            navActivityResult.setOnClickListener { navigateToActivity(ReceiverActivity::class) }
        }
    }

    private fun navigateToActivity(activityClass: KClass<*>, vararg flags: Int){
        val intent = Intent(this, activityClass.java)
        for (flag in flags) {
            intent.addFlags(flag)
        }
        startActivity(intent)
    }
}