package com.rowicka.newthings.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.contacts.model.Event
import com.rowicka.newthings.contacts.model.Navigation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

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
            moveToDetails()
        }
    }

    fun moveToDetails() {
        navigation.value = Event(Navigation.DETAIL)
    }

    fun moveToList() {
        navigation.value = Event(Navigation.LIST)
    }

    fun moveToRegister() {
        navigation.postValue(Event(Navigation.REGISTER))
    }
}