package com.rowicka.newthings.contacts.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amulyakhare.textdrawable.TextDrawable
import com.rowicka.newthings.contacts.ContactsViewModel
import com.rowicka.newthings.contacts.getInitialsFromName
import com.rowicka.newthings.contacts.phoneToColor
import com.rowicka.newthings.databinding.FragmentDetailContactBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailContactBinding
    private val mViewModel by lazy { ViewModelProvider(requireActivity()).get(ContactsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailContactBinding.inflate(layoutInflater, container, false).apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!mViewModel.contactModel.name.isEmpty()) {
            setImage()
        }

    }

    private fun setImage() {
        val contact = mViewModel.contactModel
        val contactColor = contact.phone.phoneToColor()

        binding.contactImage.apply {
            contact.photoUri?.let { setImageURI(it) } ?: setImageByInitials(
                contact.name,
                contactColor
            )

            setBackgroundColor(contactColor)
        }
    }

    private fun setImageByInitials(name: String, contactColor: Int) {
        val initialsDrawable = TextDrawable
            .builder()
            .buildRect(
                name.getInitialsFromName(),
                contactColor
            )
        binding.contactImage.setImageDrawable(initialsDrawable)
    }
}