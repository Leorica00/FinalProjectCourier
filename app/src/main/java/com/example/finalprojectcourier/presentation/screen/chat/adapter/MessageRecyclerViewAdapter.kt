package com.example.finalprojectcourier.presentation.screen.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcourier.databinding.ReceiveMessageRecyclerItemBinding
import com.example.finalprojectcourier.databinding.SendMessageRecyclerItemBinding
import com.example.finalprojectcourier.presentation.model.chat.Message
import com.google.firebase.auth.FirebaseAuth

class MessageRecyclerViewAdapter: ListAdapter<Message, RecyclerView.ViewHolder>(
    MessageItemDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_RECEIVE -> ReceiveMessageViewHolder(ReceiveMessageRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
            binding.tvSendMessage.text = currentList[adapterPosition].message
        }
    }

    inner class ReceiveMessageViewHolder(private val binding: ReceiveMessageRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            binding.tvReceiveMessage.text = currentList[adapterPosition].message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = currentList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            ITEM_SENT
        } else{
            ITEM_RECEIVE
        }
    }

    companion object {
        const val ITEM_RECEIVE = 1
        const val ITEM_SENT = 2
        private val MessageItemDiffCallback = object : DiffUtil.ItemCallback<Message>() {

            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.senderId == newItem.senderId
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}