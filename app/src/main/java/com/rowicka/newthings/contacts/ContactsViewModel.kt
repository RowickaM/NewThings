package com.rowicka.newthings.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rowicka.newthings.contacts.model.ContactsInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {

    var currentVisit: MutableSharedFlow<ContactsInfo> = MutableSharedFlow()
    var contactModel: ContactsInfo = ContactsInfo()

    var contactsList: MutableLiveData<List<ContactsInfo>> = MutableLiveData()

    fun updateList(newContacts: List<ContactsInfo>) {
        contactsList.value = newContacts

    }

    fun onContactClicked(position: Int) {
        contactsList.value?.get(position)?.let {
            viewModelScope.launch {
                contactModel = it
                currentVisit.emit(it)
            }
        }
    }
}