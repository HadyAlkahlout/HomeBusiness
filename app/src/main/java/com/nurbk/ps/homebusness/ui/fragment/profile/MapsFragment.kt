package com.nurbk.ps.homebusness.ui.fragment.profile

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdate

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentMapsBinding
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.model.address.Location
import com.nurbk.ps.homebusness.ui.fragment.dialog.ConfirmLocationFragment
import com.nurbk.ps.homebusness.ui.viewmodel.profile.MapViewModel
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.fragment_add_address.*


class MapsFragment : Fragment(), ConfirmLocationFragment.GoFragmentMessage {

    private lateinit var mBinding: FragmentMapsBinding
    lateinit var point: Marker
    lateinit var latLng: LatLng

    private val bundle by lazy {
        Bundle()
    }

    private val share by lazy {
        Constant.getSharePref(requireActivity())
    }

    private val edit
            by lazy { Constant.editor(requireActivity()) }

    private val viewModel by lazy {
        ViewModelProvider(this)[MapViewModel::class.java]
    }

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnMapClickListener {
            val location = Geocoder(requireActivity())
            try {
                googleMap.clear()
                val loc = location.getFromLocation(it.latitude+1, it.longitude+1, 10)
                val address = loc.get(0)
                val streetAddress = address.getAddressLine(0)
                latLng = it
                point = googleMap.addMarker(
                        MarkerOptions().position(it).title(streetAddress).draggable(true)
                ).also {
                    mBinding.txtContanier.visibility = View.GONE
                    ConfirmLocationFragment(this).show(childFragmentManager, "")
                }
            } catch (e: Exception) {
                Log.e("eee long catch", e.message.toString())
            }
        }

        viewModel.dataMapLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                googleMap.addMarker(MarkerOptions().position(it).title("Your Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 12F))
            }
        })


    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMapsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.dataMapStatusLiveData.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                Snackbar.make(mBinding.root, requireContext().
                getString(R.string.pleasetryagain), Snackbar.LENGTH_INDEFINITE)
                    .setAction(requireContext().getString(R.string.tryagain)) {
                        viewModel.getLocation(requireContext())
                    }.show()
            }else{
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            }
        })
    }

    override fun onClick(type: Int) {
        when (type) {
            1 -> {
                val intent = requireActivity().intent
                val location = Location(latLng)
                val address  =bundle.getParcelable<Content>("address")
                Log.e("address map",address.toString())
                intent.putExtra("address",address )
                intent.putExtra("latlng", location)
                edit.putString("lat",latLng.latitude.toString())
                edit.putString("lng",latLng.longitude.toString())
                edit.apply()
                findNavController().navigateUp()
            }
        }
    }
}