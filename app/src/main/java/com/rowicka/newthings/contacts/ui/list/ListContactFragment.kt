package com.rowicka.newthings.contacts.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rowicka.newthings.R
import com.rowicka.newthings.contacts.ContactsAdapter
import com.rowicka.newthings.contacts.ContactsViewModel
import com.rowicka.newthings.databinding.FragmentListContactBinding

class ListContactFragment : Fragment() {

    private lateinit var binding: FragmentListContactBinding
    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java) }
    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.apply {
            contactsShowRegisterButton.setOnClickListener { showCallRegister() }
        }
    }

    private fun initList(){
        adapter = ContactsAdapter().apply {
            setClickListener{
                viewModel.onContactClicked(it)
                showContactDetails()
            }
        }
        binding.contactsRecycleView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.contactsList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    private fun showCallRegister(){
        findNavController().navigate(R.id.action_listContactFragment_to_registerCallFragment)
    }

    private fun showContactDetails(){
        findNavController().navigate(R.id.action_listContactFragment_to_detailFragment)
    }
}