package com.example.finalprojectcourier.presentation.screen.forgot_password.fragment

import androidx.fragment.app.viewModels
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.screen.forgot_password.viewmodel.ForgotPasswordViewModel
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    private val viewModel: ForgotPasswordViewModel by viewModels()
    override fun setUp() {
        binding.tvSmsNumber.text = resources.getText(R.string.dot_sms).toString().plus("4569")
    }

    override fun setUpListeners() {
    }

    override fun setUpObservers() {
    }


}