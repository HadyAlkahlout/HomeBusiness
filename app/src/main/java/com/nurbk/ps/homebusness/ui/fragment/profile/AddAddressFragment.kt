package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentAddAddressBinding
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.model.address.Location
import com.nurbk.ps.homebusness.model.postaddress.Content
import com.nurbk.ps.homebusness.ui.fragment.dialog.CountryDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.profile.AddressViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.EDITADDRESS
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_add_address.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import timber.log.Timber
import java.nio.file.Files.delete

class AddAddressFragment : Fragment(), CountryDialogFragment.OnClickCountry {


    private lateinit var mBinding: FragmentAddAddressBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AddressViewModel::class.java]
    }


    private val bundle by lazy {
        Bundle()
    }

    private val intent by lazy {
        requireActivity().intent
    }

    private val arg by lazy {
        intent.getParcelableExtra<Location>("latlng")
    }

    private val arg_edit by lazy {
        requireArguments().getParcelable<com.nurbk.ps.homebusness.model.address.Content>(EDITADDRESS)
    }


    private val arg_from_map by lazy {
        intent.getParcelableExtra<Content>(EDITADDRESS)
    }


    var regionId = ""
    var countryId = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAddAddressBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        line2.setOnClickListener {
            CountryDialogFragment(this, 1).show(requireActivity().supportFragmentManager, "")
        }


        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }



        if (arg_from_map != null) {
            editTextTextPersonName.setText(arg_from_map!!.title)
            editTextTextPersonStreet.setText(arg_from_map!!.street)
            editTextTextPersonAvenue.setText(arg_edit!!.avenue)
            editTextTextPersonhouse.setText(arg_from_map!!.address)
            editTextTextPersonflatNumber.setText(arg_from_map!!.flat)
            editTextPhoneNumber.setText(arg_from_map!!.mobile)
        }


        if (arg_edit != null) {
            //Update location
            Log.e("eee edit address",arg_edit.toString())
            toolbar.menu.findItem(R.id.delete).setOnMenuItemClickListener {
                viewModel.deleteAddress(arg_edit!!.id.toString())
                return@setOnMenuItemClickListener false
            }


            editTextTextPersonName.setText(arg_edit!!.title)
            editTextTextPersonStreet.setText(arg_edit!!.street)
            editTextTextPersonAvenue.setText(arg_edit!!.avenue)
            editTextTextPersonhouse.setText(arg_edit!!.address)
            editTextTextPersonflatNumber.setText(arg_edit!!.flat)
            editTextPhoneNumber.setText(arg_edit!!.mobile)

            editTextPhoneNumber.visibility =View.GONE
            txt.visibility =View.GONE

            btn_add_address.text = getString(R.string.updated)

            btn_add_address.setOnClickListener {
                viewModel.UpdateAddress(
                        Content(arg_edit!!.id.toString(),
                                editTextTextPersonName.text.toString(),
                                arg_edit!!.city_id,
                                arg_edit!!.region_id,
                                Constant.getSharePref(requireContext()).getString("lat","").toString(),
                                Constant.getSharePref(requireContext()).getString("lng","").toString(),
                                editTextTextPersonStreet.text.toString(),
                                "4324254",
                                "4324254",
                                editTextTextPersonhouse.text.toString(),
                                editTextTextPersonflatNumber.text.toString(),
                                "4324254",
                                editTextTextPersonAvenue.text.toString()
                        ,editTextPhoneNumber.text.toString())
                )
            }

        } else {
            // post New Location

            toolbar.menu.findItem(R.id.delete).isVisible = false

            btn_add_address.setOnClickListener {
                viewModel.PostAddress(
                        Content("",
                                editTextTextPersonName.text.toString(),
                                countryId,
                                regionId,
                                Constant.getSharePref(requireContext()).getString("lat","").toString(),
                               Constant.getSharePref(requireContext()).getString("lng","").toString(),
                                editTextTextPersonStreet.text.toString(),
                                "4324254",
                                "4324254",
                                editTextTextPersonhouse.text.toString(),
                                editTextTextPersonflatNumber.text.toString(),
                                "4324254",
                                editTextTextPersonAvenue.text.toString(),
                                editTextPhoneNumber.text.toString()
                        )
                )
            }

        }


        viewModel.dataDeleteAddressLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        dialog.dismiss()
                        if (data.status) {
                            Log.e("eee delete success", data.status.toString())
                        }
                        findNavController().navigateUp()
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "try again later", Toast.LENGTH_SHORT).show()
                    Log.e("eee delete response", response.message.toString())
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                    viewModel.getAddress()
                }
            }
        })



        viewModel.dataUpdateAddressLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        dialog.dismiss()
                        if (data.status) {
                            Log.e("eee update success", data.status.toString())
                        }
                        findNavController().navigateUp()
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "try again later", Toast.LENGTH_SHORT).show()
                    Log.e("eee response", response.message.toString())
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                }
            }
        })


        viewModel.dataPostAddressLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        dialog.dismiss()
                        if (data.status) {
                            Snackbar.make(requireView(), getString(R.string.add_location_success), Snackbar.LENGTH_SHORT).show()
                        }
                        Log.e("eee added success", data.status.toString())
                        Log.e("eee added success ss", response.message.toString())
                        viewModel.getAddress()
                    }
                    findNavController().navigateUp()
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Log.e("eee response", response.message.toString())
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                }
            }
        })

        viewModel.dataGetPrimistionLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.dataGetPrimistionLiveData.postValue(false)
                val address = com.nurbk.ps.homebusness.model.address.Content(
                        "",
                        editTextTextPersonName.text.toString()
                        ,
                        countryId
                        ,regionId,
                        ""
                        ,"",
                        editTextTextPersonStreet.text.toString(),
                        editTextTextPersonName.text.toString(),
                        "",
                        editTextTextPersonhouse.text.toString(),
                        editTextTextPersonflatNumber.text.toString(),
                        "1",
                        editTextTextPersonAvenue.text.toString(),
                        editTextPhoneNumber.text.toString()
                )

                bundle.putParcelable("address", address)
                findNavController().navigate(R.id.action_addAddressFragment_to_mapsFragment, bundle)
            }
        })


        btn_give_latlng.setOnClickListener {
            val title = editTextTextPersonName.text.toString()
            val street = editTextTextPersonStreet.text.toString()
            val house = editTextTextPersonhouse.text.toString()
            val flat = editTextTextPersonflatNumber.text.toString()
            val phone = editTextPhoneNumber.text.toString()
            if (title.isEmpty()) {
                editTextTextPersonName.error = "هذا الحقل مطلوب"
                editTextTextPersonName.requestFocus()
                return@setOnClickListener
            }

            if (street.isEmpty()) {
                editTextTextPersonStreet.error = "هذا الحقل مطلوب"
                editTextTextPersonStreet.requestFocus()
                return@setOnClickListener
            }
            if (house.isEmpty()) {
                editTextTextPersonhouse.error = "هذا الحقل مطلوب"
                editTextTextPersonhouse.requestFocus()
                return@setOnClickListener
            }
            if (flat.isEmpty()) {
                editTextTextPersonflatNumber.error = "هذا الحقل مطلوب"
                editTextTextPersonflatNumber.requestFocus()
                return@setOnClickListener
            }

            if (arg_edit == null) {
                if (phone.isEmpty()) {
                    editTextPhoneNumber.error = "هذا الحقل مطلوب"
                    editTextPhoneNumber.requestFocus()
                    return@setOnClickListener
                }
            }


            if (countryId =="" && regionId ==""){
                Toast.makeText(requireContext(),"must select Country",Toast.LENGTH_SHORT).show()
            }else {
                viewModel.requestPermission(requireContext())
            }
        }


    }


    override fun onClick(data: Data, region: Region) {
        if (arg_edit != null) {
            arg_edit!!.city_id = data.id.toLong().toString()
            arg_edit!!.region_id = region.id.toLong().toString()
        }
        countryId = data.id.toString()
        regionId = region.id.toString()
    }


}