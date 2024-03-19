package com.example.finalprojectcourier.presentation.screen.splash.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.final_project.presentation.event.splash.SplashNavigationEvents
import com.example.finalprojectcourier.presentation.screen.splash.viewmodel.SplashViewModel
import com.example.finalprojectcourier.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModels()

    override fun setUp() {
    }

    override fun setUpListeners() {
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collect {
                        handleNavEvents(it)
                    }
                }

//                launch {
////                    viewModel.settingsStateFlow.collect {
////                        d("bruhWtf", it.toString())
////                        handleState(it)
//                    }
            }
        }
    }

//    private fun handleState(state: SettingsState) {
//        state.isDarkMode?.let {
//            changeDarkMode(it)
//        }
//
//        state.language?.let {
//            applyLanguageSetting(it)
//        }
//    }

//    private fun applyLanguageSetting(language: String) {
//        val lang = if (language == "English" || language == "ინგლისური") "en" else "ka"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requireContext().getSystemService(LocaleManager::class.java)
//                .applicationLocales = LocaleList.forLanguageTags(lang)
//        } else {
//            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
//        }
//    }
//
//    private fun changeDarkMode(isChecked: Boolean) {
//        if (isChecked) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//    }


    private fun handleNavEvents(events: SplashNavigationEvents) {
        when (events) {
            is SplashNavigationEvents.NavigateToLogIn -> {
                findNavController().navigate(SplashFragmentDirections.actionGlobalToWelcomeFragment())
            }

            is SplashNavigationEvents.NavigateToHome -> {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToPlaceholderDestination())
            }

            else -> {}
        }
    }
}