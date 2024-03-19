package com.example.finalprojectcourier.presentation.screen.passcode.fragment

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.PasscodeEvent
import com.example.finalprojectcourier.presentation.screen.passcode.viewmodel.PasscodeViewModel
import com.example.finalprojectcourier.presentation.screen.passcode.adapter.PasscodeRecyclerViewAdapter
import com.example.finalprojectcourier.presentation.screen.passcode.viewmodel.PasscodeNavigationEvents
import com.example.final_project.presentation.state.AuthState
import com.example.final_project.presentation.state.PasscodeState
import com.example.finalprojectcourier.NavGraphDirections
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentPasscodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasscodeFragment : BaseFragment<FragmentPasscodeBinding>(FragmentPasscodeBinding::inflate) {
    private val viewModel: PasscodeViewModel by viewModels()
    private val passcodeAdapter = PasscodeRecyclerViewAdapter()
    private val args: PasscodeFragmentArgs by navArgs()

    override fun setUp() {
        with(binding.recyclerViewPasscode) {
            layoutManager = object : LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false) { override fun canScrollHorizontally() = false }
            adapter = passcodeAdapter
        }
    }

    override fun setUpListeners() = with(binding) {
        passcodeAdapter.onTextChangeListener = {
            viewModel.onEvent(PasscodeEvent.ChangeTextInputEvent(it))
        }

        btnGoBack.setOnClickListener {
            viewModel.onUiEvent(PasscodeNavigationEvents.NavigateBack)
        }

        btnNext.setOnClickListener {
            val smsCodeUserInput = viewModel.passcodeStateFlow.value.passcode.joinToString()

            if (args.phoneNumber == null) {
                viewModel.onEvent(PasscodeEvent.SignInWithVerificationCode(args.verificationId!!, smsCodeUserInput))
            } else {
                viewModel.onEvent(PasscodeEvent.SignUp(args.verificationId!!, smsCodeUserInput))

            }
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.passcodeStateFlow.collect {
                        handleState(it)
                    }
                }

                launch {
                    viewModel.navigationEvent.collect {
                        handleNavigationState(state = it)
                    }
                }

                launch {
                    viewModel.authStateFlow.collect {
                        handleAuthState(state = it)
                    }
                }
            }
        }
    }

    private fun handleNavigationState(state: PasscodeNavigationEvents) {
        when (state) {
            is PasscodeNavigationEvents.NavigateBack -> {
                findNavController().navigateUp()
            }

            is PasscodeNavigationEvents.NavigateToSignUpCredentialsPage -> {
                findNavController().navigate(PasscodeFragmentDirections.actionPasscodeFragmentToSignUpCredentialsFragment(state.phoneNumber))
            }

            is PasscodeNavigationEvents.NavigateToHomePage -> {
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                    NavGraphDirections.actionGlobalToHomeFragment())
            }
        }
    }

    private fun handleState(state: PasscodeState) {
        passcodeAdapter.submitList(state.passcode)

        with(binding.tvResponseMessage) {
            state.errorMessage?.let {
                text = handleStringResource(it)
                setTextColor(Color.RED)
                viewModel.onEvent(PasscodeEvent.ResetPasscode)
            }

            state.successMessage?.let {
                binding.btnNext.isClickable = true
                binding.btnNext.setBackgroundResource(R.drawable.button_background)
            }
        }
    }

    private fun handleAuthState(state: AuthState) {
        with(binding) {
            progressBar.isVisible = state.isLoading

            state.errorMessage?.let {
                // handle errors
            }
        }
    }

    private fun handleStringResource(stringResource: Int): String {
        return resources.getString(stringResource)
    }
}