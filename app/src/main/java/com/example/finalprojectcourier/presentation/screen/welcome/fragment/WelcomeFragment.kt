package com.example.finalprojectcourier.presentation.screen.welcome.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.screen.welcome.viewmodel.WelcomeUiEvent
import com.example.finalprojectcourier.presentation.screen.welcome.viewmodel.WelcomeViewModel
import com.example.finalprojectcourier.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    private val viewModel: WelcomeViewModel by viewModels()

    override fun setUp() {}

    override fun setUpListeners() = with(binding) {
        btnGoToSignIn.setOnClickListener {
            viewModel.onUiEvent(WelcomeUiEvent.NavigateToLoginFragment)
        }
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    handleNavigationEvents(event = it)
                }
            }
        }
    }

    private fun handleNavigationEvents(event: WelcomeUiEvent) {
        when (event) {
            is WelcomeUiEvent.NavigateToLoginFragment -> {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
            }
        }
    }
}