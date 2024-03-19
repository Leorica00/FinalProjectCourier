package com.example.finalprojectcourier.presentation.screen.login.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.LoginEvent
import com.example.finalprojectcourier.presentation.screen.login.viewmodel.LoginFragmentUiEvents
import com.example.finalprojectcourier.presentation.screen.login.viewmodel.LoginViewModel
import com.example.final_project.presentation.state.VerificationState
import com.example.finalprojectcourier.NavGraphDirections
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class  LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun setUp() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    override fun setUpListeners() = with(binding) {
        tvForgotPass.setOnClickListener {
            viewModel.onUiEvent(LoginFragmentUiEvents.ForgotPassword)
        }

        tvRegister.setOnClickListener {
            viewModel.onUiEvent(LoginFragmentUiEvents.NavigateToSignUpPage)
        }

        btnContinue.setOnClickListener {
            val credential = binding.etCredentials.text.toString()
            viewModel.onEvent(LoginEvent.SignInUserWithCredential(credential, providePhoneAuthOptionsBuilder(credential)))
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
            credentialsContainer.error = resources.getString(it)
        }
    }

    private fun handleNavigationEvents(events: LoginFragmentUiEvents) {
        when (events) {
            is LoginFragmentUiEvents.NavigateToSignUpPage -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }

            is LoginFragmentUiEvents.NavigateToHomePage -> {
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.actionGlobalToHomeFragment())
            }

            is LoginFragmentUiEvents.NavigateToSmsAuthPage -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPasscodeFragment(verificationId = events.verificationId, phoneNumber = null))
            }

            is LoginFragmentUiEvents.ForgotPassword -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }

            is LoginFragmentUiEvents.NavigateToEmailPasswordPage -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPasswordFragment(events.email))
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