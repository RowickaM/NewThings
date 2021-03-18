package com.rowicka.newthings.contacts.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rowicka.newthings.contacts.ContactsViewModel
import com.rowicka.newthings.databinding.FragmentDetailContactBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailContactBinding
//    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.contactModel.apply {
//            binding.textView2.text = name
//        }

    }
}