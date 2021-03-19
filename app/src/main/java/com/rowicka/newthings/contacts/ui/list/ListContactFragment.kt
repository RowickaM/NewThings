package com.rowicka.newthings.contacts.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rowicka.newthings.R
import com.rowicka.newthings.contacts.ContactsViewModel
import com.rowicka.newthings.contacts.adapter.ContactsAdapter
import com.rowicka.newthings.contacts.adapter.OnRecyclerListener
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.contacts.model.Navigation
import com.rowicka.newthings.databinding.FragmentContactsBinding
import com.rowicka.newthings.utils.observeEvent

class ListContactFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private val mViewModel by lazy { ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner

            contactsRecycleView.adapter = ContactsAdapter()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            contactsShowRegisterButton.setOnClickListener { mViewModel.registerClicked() }
        }

        mViewModel.apply {
            observeEvent(navigation, ::observeNavigation)
        }
    }

    private fun observeNavigation(navigation: Navigation?) {
        when (navigation) {
            Navigation.DETAIL -> showContactDetails()
            Navigation.REGISTER -> showCallRegister()
        }
    }

    private fun showCallRegister() {
        findNavController().navigate(R.id.action_listContactFragment_to_registerCallFragment)
    }

    private fun showContactDetails() {
        findNavController().navigate(R.id.action_listContactFragment_to_detailFragment)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("data")
fun setRecyclerData(recyclerView: RecyclerView, items: List<ContactsInfo>?) {
    if (recyclerView.adapter is ContactsAdapter) {
        items?.let {
            (recyclerView.adapter as ContactsAdapter).setList(it)
        }
    }
}

@BindingAdapter("onRecyclerClick")
fun setViewPagerListener(recyclerView: RecyclerView, onRecyclerListener: OnRecyclerListener) {
    if (recyclerView.adapter is ContactsAdapter) {
        (recyclerView.adapter as ContactsAdapter).setClickListener(onRecyclerListener)
    }
}
