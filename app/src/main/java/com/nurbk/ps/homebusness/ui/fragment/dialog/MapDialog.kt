package com.nurbk.ps.homebusness.ui.fragment.dialog

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.DialogMapBinding
import com.nurbk.ps.homebusness.ui.viewmodel.profile.MapViewModel

class MapDialog(val onClick: OnClickSaveLocationMap) : DialogFragment() {

    private lateinit var mBinding: DialogMapBinding
    lateinit var point: Marker
    lateinit var latLng: LatLng
    private val viewModel by lazy {
        ViewModelProvider(this)[MapViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogMapBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(callback)


        mBinding.btnSaveLocation.setOnClickListener {
            onClick.getLocationMap(latLng)
            dismiss()
        }
    }


    private val callback = OnMapReadyCallback { googleMap ->


        googleMap.setOnMapClickListener {
            val location = Geocoder(requireActivity())
            try {
                googleMap.clear()
                val loc = location.getFromLocation(it.latitude, it.longitude, 10)
                val address = loc[0]
                val streetAddress = address.getAddressLine(0)
                latLng = it
                point = googleMap.addMarker(
                    MarkerOptions().position(it).title(streetAddress).draggable(true)
                ).also {
                    mBinding.txtContanier.visibility = View.GONE
                    mBinding.btnSaveLocation.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
            }
        }


        viewModel.dataMapLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                googleMap.addMarker(MarkerOptions().position(it).title("Your Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 12F))
            }
        })


    }

    interface OnClickSaveLocationMap {
        fun getLocationMap(latLng: LatLng)
    }

}