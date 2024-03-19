package com.example.finalprojectcourier.presentation.screen.chat.chat_contacts

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.chat.ChatContactEvent
import com.example.finalprojectcourier.presentation.screen.chat.adapter.ContactsRecyclerViewAdapter
import com.example.final_project.presentation.state.ContactsState
import com.example.finalprojectcourier.databinding.FragmentChatContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatContactsFragment : BaseFragment<FragmentChatContactsBinding>(FragmentChatContactsBinding::inflate) {

    private val viewModel: ChatContactsViewModel by viewModels()
    private val contactsAdapter = ContactsRecyclerViewAdapter()
    override fun setUp() {
        setUpRecycler()
        viewModel.onEvent(ChatContactEvent.GetContactsEvent)
//        viewModel.onEvent(ChatContactEvent.AddContactEvent(Contact(null, "", "YeMuykw1zFgHOQrBSVP3Vd5XkPq2", "Goga Gradienti")))
    }

    override fun setUpListeners() {
//        contactsAdapter.onHumanContactClick = {
//            findNavController().navigate(ChatContactsFragmentDirections.actionChatPageToChatFragment(
//                uuid = it.receiverId!! ,
//                fullName = it.fullName!!,
//                imageUrl = it.imageUrl
//            ))
//        }
//
//        contactsAdapter.onChatbotContactClick = {
//            findNavController().navigate(ChatContactsFragmentDirections.actionChatPageToChatBotFragment())
//        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsStateFlow.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(state: ContactsState) {
        with(state) {
            contacts?.let {
                contactsAdapter.submitList(it)
            }
        }
    }

    private fun setUpRecycler() {
        with(binding.recyclerViewContacts) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }
    }

}