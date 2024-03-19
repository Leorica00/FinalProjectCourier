package com.example.finalprojectcourier.presentation.screen.chat.chat_messaging.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.chat.ChatEvent
import com.example.final_project.presentation.extension.loadImage
import com.example.finalprojectcourier.presentation.model.chat.Message
import com.example.finalprojectcourier.presentation.screen.chat.adapter.MessageRecyclerViewAdapter
import com.example.finalprojectcourier.presentation.screen.chat.chat_messaging.viewmodel.ChatViewModel
import com.example.final_project.presentation.state.ChatState
import com.example.finalprojectcourier.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val viewModel: ChatViewModel by viewModels()
//    private val safeArgs: ChatFragmentArgs by navArgs()
    private lateinit var receiverId: String
    private val messageAdapter = MessageRecyclerViewAdapter()

    override fun setUp() {
        setUpContact()
        setUpRecycler()
    }

    override fun setUpListeners() {
        setUpMessageSentListener()
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatStateFlow.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(state: ChatState) {
        with(state) {
            messages?.let {
                messageAdapter.submitList(it)
                binding.charRecyclerView.scrollToPosition(-1)
            }
        }
    }

    private fun setUpContact() {
//        receiverId = safeArgs.uuid
//        viewModel.getReceiverId(receiverId)
//        with(binding) {
//            safeArgs.imageUrl?.let {
//                imageViewProfile.loadImage(it)
//            }
//            tvProfileName.text = safeArgs.fullName
//        }
    }

    private fun setUpRecycler() {
        with(binding.charRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }

        viewModel.onEvent(ChatEvent.GetMessagesEvent(receiverId))
    }

    private fun setUpMessageSentListener() {
        binding.sentButton.setOnClickListener {
            val message = Message(Random.nextLong(1, Long.MAX_VALUE), binding.messageBox.text.toString(), FirebaseAuth.getInstance().currentUser?.uid)
            viewModel.onEvent(ChatEvent.AddMessageEvent(message, receiverId))
            binding.messageBox.text?.clear()
        }
    }
}