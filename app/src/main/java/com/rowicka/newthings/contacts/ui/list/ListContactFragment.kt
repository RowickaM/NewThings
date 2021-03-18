package com.rowicka.newthings.contacts.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rowicka.newthings.R
import com.rowicka.newthings.contacts.ContactsAdapter
import com.rowicka.newthings.contacts.ContactsViewModel
import com.rowicka.newthings.contacts.model.Navigation
import com.rowicka.newthings.databinding.FragmentListContactBinding
import com.rowicka.newthings.utils.observeEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        initObservers()
    }

    private fun initList(){
        adapter = ContactsAdapter().apply {
            setClickListener{
                viewModel.onContactClicked(it)
            }
        }
        binding.contactsRecycleView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.contactsList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

        viewModel.apply {
            observeEvent(navigation){
                if(it == Navigation.DETAIL){
                    showContactDetails()
                }
            }
        }
    }


    private fun showContactDetails(){
        findNavController().navigate(R.id.action_listContactFragment_to_detailFragment)
    }
}