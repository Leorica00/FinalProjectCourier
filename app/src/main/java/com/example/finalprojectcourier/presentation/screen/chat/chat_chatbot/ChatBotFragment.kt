package com.example.finalprojectcourier.presentation.screen.chat.chat_chatbot

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.bot.ChatBotEvents
import com.example.final_project.presentation.extension.showSnackBar
import com.example.finalprojectcourier.presentation.screen.chat.adapter.ChatBotMessageRecyclerViewAdapter
import com.example.final_project.presentation.state.ChatBotState
import com.example.finalprojectcourier.databinding.FragmentChatBotBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class ChatBotFragment : BaseFragment<FragmentChatBotBinding>(FragmentChatBotBinding::inflate) {

    private val viewModel: ChatBotViewModel by viewModels()
    private val chatBotAdapter = ChatBotMessageRecyclerViewAdapter()
    override fun setUp() {
        viewModel.onEvent(ChatBotEvents.UpdateSessionIdEvent(sessionId = UUID.randomUUID().toString()))
        setUpChatBotRecycler()
    }

    override fun setUpListeners() {
        binding.sentButton.setOnClickListener {
            viewModel.onEvent(ChatBotEvents.SendTextToChatBotEvent(binding.messageBox.text.toString()))
            binding.messageBox.text?.clear()
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatBotStateFlow.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(state: ChatBotState) = with(state) {
        messages?.let {
            chatBotAdapter.submitList(it.toList())
        }

        errorMessage?.let {
            requireView().showSnackBar(resources.getString(it))
            viewModel.onEvent(ChatBotEvents.UpdateErrorMessage(null))
        }
    }

    private fun setUpChatBotRecycler() = with(binding.chatRecyclerView) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = chatBotAdapter
    }

}