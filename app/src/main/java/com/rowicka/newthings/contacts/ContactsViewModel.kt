package com.rowicka.newthings.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rowicka.newthings.contacts.model.ContactsInfo

class ContactsViewModel : ViewModel() {

    var contactModel: ContactsInfo = ContactsInfo()

    var contactsList: MutableLiveData<List<ContactsInfo>> = MutableLiveData()


    fun updateList(newContacts: List<ContactsInfo>) {
        contactsList.value = newContacts

    }

    fun onContactClicked(position: Int) {
        contactsList.value?.get(position)?.let {
            contactModel = it
        }
    }
}