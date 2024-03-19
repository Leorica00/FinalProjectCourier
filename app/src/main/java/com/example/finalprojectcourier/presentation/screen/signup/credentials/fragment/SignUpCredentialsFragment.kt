package com.example.finalprojectcourier.presentation.screen.signup.credentials.fragment

import android.util.Log.d
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.event.signup.SendUserDataEvent
import com.example.final_project.presentation.extension.showSnackBar
import com.example.finalprojectcourier.presentation.screen.signup.credentials.viewmodel.SignUpCredentialsNavigationEvents
import com.example.finalprojectcourier.presentation.screen.signup.credentials.viewmodel.SignUpCredentialsViewModel
import com.example.finalprojectcourier.databinding.FragmentSignUpCredentialsBinding
import com.example.finalprojectcourier.presentation.state.AdditionalDataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpCredentialsFragment : BaseFragment<FragmentSignUpCredentialsBinding>(FragmentSignUpCredentialsBinding::inflate) {
    private val viewModel: SignUpCredentialsViewModel by viewModels()

    override fun setUp() {
    }

    override fun setUpListeners() = with(binding) {
        btnCreateAccount.setOnClickListener {
            viewModel.onEvent(
                SendUserDataEvent.SendUserData(
                email = etEmail.text.toString(),
                password = etPassword.text.toString(),
                fullName = etFullName.text.toString()
            ))
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigationEvent.collect {
                        handleNavigationEvents(events = it)
                    }
                }

                launch {
                    viewModel.dataState.collect {
                        handleAdditionalDataState(state = it)
                    }
                }
            }
        }
    }

    private fun handleAdditionalDataState(state: AdditionalDataState) = with(state) {
        with(binding) {
            d("errorState", state.toString())
            progressBar.isVisible = isLoading

            errorMessage?.let {
                progressBar.visibility = View.GONE
                requireView().showSnackBar(getStringResource(it)!!)
            }

            etEmailContainer.error = getStringResource(emailErrorMessage)

            fullNameContainer.error = getStringResource(fullNameErrorMessage)

            passwordContainer.error = getStringResource(passwordErrorMessage)
        }
    }

    private fun getStringResource(@StringRes stringRes: Int?): String? {
        stringRes?.let {
            return resources.getString(it)
        }
        return null
    }

    private fun handleNavigationEvents(events: SignUpCredentialsNavigationEvents) {
        when (events) {
            is SignUpCredentialsNavigationEvents.NavigateToSuccessPage -> {
                findNavController().navigate(SignUpCredentialsFragmentDirections.actionSignUpCredentialsFragmentToSignUpSuccessFragment())
            }
        }
    }
}