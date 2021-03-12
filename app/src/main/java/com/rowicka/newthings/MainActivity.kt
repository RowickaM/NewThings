package com.rowicka.newthings

import android.os.Bundle
import com.rowicka.newthings.activityResult.HomeScreenActivity
import com.rowicka.newthings.customView.CustomViewActivity
import com.rowicka.newthings.databinding.ActivityMainBinding
import com.rowicka.newthings.drawOnCanvas.DrawCanvasActivity
import com.rowicka.newthings.todoapp.tasks.TasksActivity
import com.rowicka.newthings.utils.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners(){
        binding.apply {
            navActivityResult.setOnClickListener { navigateToActivity(HomeScreenActivity::class) }
            navCustomView.setOnClickListener { navigateToActivity(CustomViewActivity::class) }
            navDrawOnCanvas.setOnClickListener { navigateToActivity(DrawCanvasActivity::class) }
            navTodoApp.setOnClickListener { navigateToActivity(TasksActivity::class) }
        }
    }
}