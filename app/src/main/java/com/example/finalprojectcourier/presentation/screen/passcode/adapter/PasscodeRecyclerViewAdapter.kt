package com.example.finalprojectcourier.presentation.screen.passcode.adapter

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.final_project.presentation.model.Passcode
import com.example.finalprojectcourier.databinding.RecyclerPasscodeItemBinding

class PasscodeRecyclerViewAdapter: ListAdapter<Passcode, PasscodeRecyclerViewAdapter.PasscodeViewHolder>(
    CategoriesItemDiffCallback
) {

    var onTextChangeListener: ((Passcode) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasscodeViewHolder {
        return PasscodeViewHolder(RecyclerPasscodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PasscodeViewHolder, position: Int) {
        holder.bind()
    }

    inner class PasscodeViewHolder(private val binding: RecyclerPasscodeItemBinding): ViewHolder(binding.root) {
        fun bind() {
            onDeleteListener()
            with(binding.etPasscode) {
                setText(currentList[adapterPosition].currentNumber?.toString() ?: "")
                doOnTextChanged { text, start, before, count ->
                    val passcode = currentList[adapterPosition].copy(
                        currentNumber = text.toString().toIntOrNull()
                    )
                    onTextChangeListener?.invoke(passcode)
                    when {
                        count == 1 && adapterPosition < currentList.size - 1 -> (itemView.focusSearch(
                            View.FOCUS_RIGHT
                        ) as? EditText)?.requestFocus()
                        adapterPosition == currentList.size - 1 -> hideKeyboard()
                    }
                }
            }
        }

        private fun onDeleteListener() {
            binding.etPasscode.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL && binding.etPasscode.text.isNullOrEmpty() && adapterPosition > 0) {
                    (itemView.focusSearch(View.FOCUS_LEFT) as? EditText)?.apply {
                        requestFocus()
                        setSelection(text?.length ?: 0)
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        private fun hideKeyboard() {
            val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etPasscode.windowToken, 0)
            binding.etPasscode.clearFocus()
        }
    }

    companion object {
        private val CategoriesItemDiffCallback = object : DiffUtil.ItemCallback<Passcode>() {

            override fun areItemsTheSame(oldItem: Passcode, newItem: Passcode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Passcode, newItem: Passcode): Boolean {
                return oldItem == newItem
            }
        }
    }
}