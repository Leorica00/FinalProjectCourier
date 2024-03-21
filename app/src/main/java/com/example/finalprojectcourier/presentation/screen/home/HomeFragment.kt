package com.example.finalprojectcourier.presentation.screen.home

import androidx.navigation.fragment.findNavController
import com.example.finalprojectcourier.databinding.FragmentHomeBinding
import com.example.finalprojectcourier.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun setUp() {
    }

    override fun setUpListeners() {
        binding.btnStartWorking.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCourierDeliveryMapFragment())
        }
    }

    override fun setUpObservers() {
    }
}