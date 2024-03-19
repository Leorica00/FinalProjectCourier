package com.example.finalprojectcourier.presentation.screen.signup.success.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.finalprojectcourier.NavGraphDirections
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.screen.signup.success.viewmodel.SignUpSuccessNavigationEvents
import com.example.finalprojectcourier.presentation.screen.signup.success.viewmodel.SignUpSuccessViewModel
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentSignUpSuccessBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpSuccessFragment : BaseFragment<FragmentSignUpSuccessBinding>(FragmentSignUpSuccessBinding::inflate) {
    private val viewModel: SignUpSuccessViewModel by viewModels()

    override fun setUp() {

    }

    override fun setUpListeners() = with(binding) {
        btnTryOrder.setOnClickListener {
            viewModel.onUiEvent(SignUpSuccessNavigationEvents.NavigateToHomePage)
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigationEvent.collect {
                        handleNavigationEvent(event = it)
                    }
                }
            }
        }
    }

    private fun handleNavigationEvent(event: SignUpSuccessNavigationEvents) {
        when (event) {
            is SignUpSuccessNavigationEvents.NavigateToHomePage -> {
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                    NavGraphDirections.actionGlobalToHomeFragment())
            }
        }
    }
}