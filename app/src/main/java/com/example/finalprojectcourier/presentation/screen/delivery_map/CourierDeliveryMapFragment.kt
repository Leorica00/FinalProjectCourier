package com.example.finalprojectcourier.presentation.screen.delivery_map

import android.content.Intent
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.service.DeliveryService
import com.example.finalprojectcourier.databinding.FragmentCourierDeliveryMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CourierDeliveryMapFragment : BaseFragment<FragmentCourierDeliveryMapBinding>(FragmentCourierDeliveryMapBinding::inflate)  {

    private val viewModel: CourierDeliveryMapViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        updateLocationUi(LatLng(41.7934135, 44.8025545))
    }

    override fun setUp() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = binding.map.getFragment<SupportMapFragment>()
        mapFragment.getMapAsync(callback)

        val deliveryId = "your_delivery_id"
        val serviceIntent = Intent(requireActivity(), DeliveryService::class.java)
        serviceIntent.putExtra("deliveryId", deliveryId)

        requireActivity().startForegroundService(serviceIntent)
        viewModel.getMenuUpdate()
    }

        override fun setUpListeners() {
    }

    override fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.directionStateFlow.collect {
                    it.direction?.let {
                        val path = PolyUtil.decode(it.routes[0].overview_polyline.points)
                        val polylineOptions = PolylineOptions()
                            .width(10f)
                            .color(Color.RED)
                            .addAll(path)

                        mMap?.clear()
                        mMap?.addPolyline(polylineOptions)
                    }
                    it.order?.let {
                        with(binding) {
                            it.isActive?.let { active ->
                                map.isVisible = active
                                progressBar.isVisible = !active
                                tvLookingForOrder.isVisible = !active
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateLocationUi(latLng: LatLng) =
        mMap?.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
}