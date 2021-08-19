package com.rowicka.newthings.notifications.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rowicka.newthings.databinding.FragmentEggTimerBinding

class EggTimerFragment : Fragment() {

    private val TOPIC = "breakfast"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(EggTimerViewModel::class.java)

        val binding: FragmentEggTimerBinding = FragmentEggTimerBinding.inflate(
            layoutInflater,
            container,
            false
        ).apply {
            eggTimerViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // TODO: Step 1.7 call create channel

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel

        // TODO: Step 1.6 END create a channel

    }

    companion object {
        fun newInstance() = EggTimerFragment()
    }
}

