package com.example.finalprojectcourier.presentation.screen.delivery_map

import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalprojectcourier.presentation.base.BaseFragment
import com.example.finalprojectcourier.presentation.service.DeliveryService
import com.example.finalprojectcourier.R
import com.example.finalprojectcourier.databinding.FragmentCourierDeliveryMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CourierDeliveryMapFragment : BaseFragment<FragmentCourierDeliveryMapBinding>(FragmentCourierDeliveryMapBinding::inflate)  {

    private val viewModel: CourierDeliveryMapViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        updateLocationUi(LatLng(41.7934135, 44.8025545))

        viewModel.getDirection(LatLng(41.79353332519531, 44.80256652832031), LatLng(41.79208755493164, 44.81571578979492))
    }

    override fun setUp() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val deliveryId = "your_delivery_id"
        val serviceIntent = Intent(requireActivity(), DeliveryService::class.java)
        serviceIntent.putExtra("deliveryId", deliveryId)

        requireActivity().startForegroundService(serviceIntent)
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

                        val bitmap = getDrawable(requireContext(), R.drawable.ic_delivery)!!.toBitmap(80, 80)
                        val markerOptions = MarkerOptions()
                            .position(LatLng(41.709904512556946, 44.79725170393272)) // Replace with your desired LatLng object (location)
                            .title("Delivery Location") // Set marker title (optional)
                            .snippet("Delivery Details") // Set marker snippet (optional)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                        mMap.clear()
                        mMap.addMarker(markerOptions)
                        mMap.addPolyline(polylineOptions)
                    }
                }
            }
        }
    }

    private fun updateLocationUi(latLng: LatLng) = with(mMap) {
        moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
    }
}