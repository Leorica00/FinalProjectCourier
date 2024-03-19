package com.example.finalprojectcourier.presentation.screen.chat.adapter

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.presentation.model.chatbot.BotResponse
import com.example.final_project.presentation.model.chatbot.SenderType
import com.example.finalprojectcourier.databinding.ReceiveMessageRecyclerItemBinding
import com.example.finalprojectcourier.databinding.SendMessageRecyclerItemBinding

class ChatBotMessageRecyclerViewAdapter: ListAdapter<BotResponse, RecyclerView.ViewHolder>(
    MessageItemDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_RECEIVE -> ReceiveMessageViewHolder(
                ReceiveMessageRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            ITEM_SENT -> SentMessageViewHolder(SendMessageRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> SentMessageViewHolder(SendMessageRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SentMessageViewHolder -> holder.bind()
            is ReceiveMessageViewHolder -> holder.bind()
        }
    }

    inner class SentMessageViewHolder(private val binding: SendMessageRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            d("MessageDaixata", "SENDER${currentList[adapterPosition].response} ${currentList}")
            binding.tvSendMessage.text = currentList[adapterPosition].response
        }
    }

    inner class ReceiveMessageViewHolder(private val binding: ReceiveMessageRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            d("MessageDaixata", "RECEIVER${currentList[adapterPosition].response} ${currentList}")
            binding.tvReceiveMessage.text = currentList[adapterPosition].response
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = currentList[position]
        return if(currentMessage.senderType == SenderType.USER){
            ITEM_SENT
        } else{
            ITEM_RECEIVE
        }
    }

    companion object {
        const val ITEM_RECEIVE = 1
        const val ITEM_SENT = 2
        private val MessageItemDiffCallback = object : DiffUtil.ItemCallback<BotResponse>() {

            override fun areItemsTheSame(oldItem: BotResponse, newItem: BotResponse): Boolean {
                return oldItem.responseId == newItem.responseId
            }

            override fun areContentsTheSame(oldItem: BotResponse, newItem: BotResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}