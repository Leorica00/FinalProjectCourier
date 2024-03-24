package com.example.finalprojectcourier.presentation.screen.chat.chat_contacts

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.remote.common.Resource
import com.example.finalprojectcourier.domain.usecase.chat.AddContactUseCase
import com.example.finalprojectcourier.domain.usecase.chat.GetContactsUseCase
import com.example.final_project.presentation.event.chat.ChatContactEvent
import com.example.final_project.presentation.mapper.chat.toDomain
import com.example.final_project.presentation.mapper.chat.toPresentation
import com.example.final_project.presentation.model.chat.Contact
import com.example.final_project.presentation.state.ContactsState
import com.example.finalprojectcourier.presentation.util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatContactsViewModel @Inject constructor(private val getContactsUseCase: GetContactsUseCase, private val addContactUseCase: AddContactUseCase): ViewModel() {
    private val _contactsStateFlow = MutableStateFlow(ContactsState())
    val contactsStateFlow = _contactsStateFlow.asStateFlow()

    fun onEvent(event: ChatContactEvent) {
        when(event) {
            is ChatContactEvent.GetContactsEvent -> getContacts()
            is ChatContactEvent.AddContactEvent -> addContact(contact = event.contact)
        }
    }

    private fun getContacts() {
        viewModelScope.launch {
            getContactsUseCase().collect {resource ->
                d("resourceContacts", resource.toString())
                when(resource) {
                    is Resource.Loading -> _contactsStateFlow.update { currentState -> currentState.copy(isLoading = resource.loading) }
                    is Resource.Success -> {
                        val currentList = mutableListOf<Contact>()
                        currentList.addAll(resource.response.map { it.toPresentation() })
                        _contactsStateFlow.update {currentState -> currentState.copy(contacts = currentList)
                        }
                    }
                    is Resource.Error -> updateErrorMessage(getErrorMessage(resource.error))
                }
            }
        }
    }

    private fun addContact(contact: Contact) {
        viewModelScope.launch {
            addContactUseCase(contact = contact.toDomain())
        }
    }

    private fun updateErrorMessage(errorMessage: Int?) {
        _contactsStateFlow.update { currentState -> currentState.copy(errorMessage = errorMessage) }
    }
}