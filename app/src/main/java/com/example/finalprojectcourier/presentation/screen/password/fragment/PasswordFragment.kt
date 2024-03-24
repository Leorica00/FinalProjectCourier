package com.example.finalprojectcourier.presentation.screen.password.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.EmailPasswordEvents
import com.example.finalprojectcourier.presentation.screen.password.viewmodel.EmailPasswordUiEvents
import com.example.finalprojectcourier.presentation.screen.password.viewmodel.PasswordViewModel
import com.example.final_project.presentation.state.LoginState
import com.example.finalprojectcourier.NavGraphDirections
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordFragment : BaseFragment<FragmentPasswordBinding>(FragmentPasswordBinding::inflate) {
    private val viewModel: PasswordViewModel by viewModels()
    private val args: PasswordFragmentArgs by navArgs()

    override fun setUp() {

    }

    override fun setUpListeners() = with(binding) {
        btnLogin.setOnClickListener {
            viewModel.onEvent(EmailPasswordEvents.SignInUsingEmailAndPassword(
                email = args.email,
                password = etPassword.text.toString()
            ))
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collect {
                        handleNavigation(it)
                    }
                }

                launch {
                    viewModel.logInState.collect {
                        handleLoginState(it)
                    }
                }
            }
        }
    }

    private fun handleLoginState(state: LoginState) = with(binding) {
        progressBar.isVisible = state.isLoading

        state.errorMessage?.let {
            progressBar.visibility = View.GONE
            passwordContainer.error = resources.getString(it)
        }
    }

    private fun handleNavigation(event: EmailPasswordUiEvents) {
        when (event) {
            is EmailPasswordUiEvents.NavigateToHomeFragment ->
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                    NavGraphDirections.actionGlobalToHomeFragment())
        }
    }
}