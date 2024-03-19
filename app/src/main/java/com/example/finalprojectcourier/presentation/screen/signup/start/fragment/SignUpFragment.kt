package com.example.finalprojectcourier.presentation.screen.signup.start.fragment

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.signup.SendSmsEvent
import com.example.final_project.presentation.extension.showSnackBar
import com.example.finalprojectcourier.presentation.screen.signup.start.viewmodel.SignUpNavigationEvents
import com.example.finalprojectcourier.presentation.screen.signup.start.viewmodel.SignUpViewModel
import com.example.final_project.presentation.state.VerificationState
import com.example.finalprojectcourier.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    private val viewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun setUp() {}

    override fun setUpListeners() = with(binding) {
        btnContinue.setOnClickListener {
            val countryCode: String = ccp.selectedCountryCodeWithPlus
            val number = etPhoneNumber.text.toString().trim()

            viewModel.onEvent(SendSmsEvent.SendSmsToProvidedNumber(phoneNumber = "$countryCode$number", options = providePhoneAuthOptionsBuilder("$countryCode$number")))
        }

        tvLogin.setOnClickListener {
            viewModel.onUiEvent(SignUpNavigationEvents.NavigateToSignInPage)
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
                    viewModel.verificationState.collect {
                        handleVerificationState(it)
                    }
                }
            }
        }
    }

    private fun handleVerificationState(verificationState: VerificationState) = with(binding) {
        progressBar.isVisible = verificationState.isLoading

        verificationState.errorMessage?.let {
            progressBar.visibility = View.GONE
            requireView().showSnackBar(getStringResource(it)!!)
        }

        phoneNumberContainerLayout.error = getStringResource(verificationState.phoneErrorMessage)
    }

    private fun getStringResource(@StringRes stringRes: Int?): String? {
        stringRes?.let {
            return resources.getString(it)
        }
        return null
    }

    private fun handleNavigationEvents(events: SignUpNavigationEvents) {
        when (events) {
            is SignUpNavigationEvents.NavigateToSignInPage -> {
                findNavController().navigateUp()
            }

            is SignUpNavigationEvents.NavigateToOtpFillPage -> {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToPasscodeFragment(
                    phoneNumber = events.phoneNumber,
                    verificationId = events.verificationId
                ))
            }
        }
    }

    private fun providePhoneAuthOptionsBuilder(phoneNumber: String) : PhoneAuthOptions.Builder {
        return PhoneAuthOptions.newBuilder(auth)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity()).
            setPhoneNumber(phoneNumber)
    }
}