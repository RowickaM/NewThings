package com.rowicka.newthings.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.contacts.model.Event
import com.rowicka.newthings.contacts.model.Navigation

class ContactsViewModel : ViewModel() {

    var contactModel: ContactsInfo = ContactsInfo()

    var contactsList: MutableLiveData<List<ContactsInfo>> = MutableLiveData()

    var navigation: MutableLiveData<Event<Navigation>> = MutableLiveData()

    fun updateList(newContacts: List<ContactsInfo>) {
        contactsList.value = newContacts
    }

    fun onContactClicked(position: Int) {
        contactsList.value?.get(position)?.let {
            contactModel = it
            navigation.value = Event(Navigation.DETAIL)
        }
    }

    fun registerClicked() {
        navigation.value = Event(Navigation.REGISTER)
    }
}