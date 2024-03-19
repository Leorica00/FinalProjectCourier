package com.example.finalprojectcourier.presentation.screen.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.presentation.extension.loadImage
import com.example.final_project.presentation.model.chat.Contact
import com.example.finalprojectcourier.databinding.ContactRecyclerItemBinding

class ContactsRecyclerViewAdapter: ListAdapter<Contact, ContactsRecyclerViewAdapter.ContactViewHolder>(
    ContactItemDiffCallback
) {

    var onHumanContactClick: ((Contact) -> Unit)? = null
    var onChatbotContactClick: ((Contact) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(ContactRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
       holder.bind()
    }

    inner class ContactViewHolder(private val binding: ContactRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            val contact = currentList[adapterPosition]
            with(binding) {
                contact.imageUrl?.let {
                    shapeableImageViewCover.loadImage(it)
                }
                tvFullName.text = contact.fullName

                if (adapterPosition == 0) {
                    root.setOnClickListener {
                        onChatbotContactClick?.invoke(contact)
                    }
                }else {
                    root.setOnClickListener {
                        onHumanContactClick?.invoke(contact)
                    }
                }

            }
        }
    }

    companion object {
        private val ContactItemDiffCallback = object : DiffUtil.ItemCallback<Contact>() {

            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.receiverId == newItem.receiverId
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }
        }
    }
}