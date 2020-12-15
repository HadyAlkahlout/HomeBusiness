package com.nurbk.ps.homebusness.ui.fragment.dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.AddressAdapter
import com.nurbk.ps.homebusness.adapter.MyExpandableClass
import com.nurbk.ps.homebusness.databinding.DialogAddressBinding
import com.nurbk.ps.homebusness.databinding.DialogCountryBinding
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.model.cart.City
import com.nurbk.ps.homebusness.ui.viewmodel.auth.SplashViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.profile.AddressViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.dialog_address.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.fragment_address.*
import timber.log.Timber


class AddressDialogFragment(val onClick: OnClickAddress, val type: Int,val array_City:ArrayList<City>,val array_region:ArrayList<com.nurbk.ps.homebusness.model.cart.Region>) : DialogFragment(),
    AddressAdapter.OnClickItem {

    private lateinit var mBinding: DialogAddressBinding

    val header = ArrayList<Data>()
    val body = ArrayList<List<Region>>()
    private val mAdapter by lazy {
        AddressAdapter(ArrayList(), this)
    }

     var array=ArrayList<Content>()

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[AddressViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogAddressBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        if (type== 0) {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAddress()

        viewModel.dataAddressLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->

                        Log.e("eee error",response.message.toString())
                        Log.e("eee error",data.status.toString())
                        if (data.status) {

                            mAdapter.data.clear()
                            data.data.data.forEach {
                                array_region.forEach {region->
                                    if (region.region_id.toString() ==it.region_id) {
                                        mAdapter.data.add(it)
                                    }
                                }
                                array_City.forEach {city->
                                    if (city.city_id.toString() ==it.city_id) {
                                        mAdapter.data.add(it)
                                    }
                                }
                            }

                            mAdapter.notifyDataSetChanged()

                            if (data.data.data.size ==0){
                                address_container.visibility=View.VISIBLE
                            }else{
                                address_container.visibility=View.GONE
                            }
                            Constant.dialog.dismiss()
                            Log.e("eee",data.data.data.toString())
                        }else{
                            Constant.dialog.dismiss()
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                    Log.e("eee error",response.message.toString())

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                    Log.e("eee error",response.message.toString())
                }
            }
        })

        rcDataAddress2.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }


        btn_add_address_dialog.setOnClickListener {
            val bundle =Bundle()
            bundle.putParcelable(Constant.EDITADDRESS,null)
            findNavController().navigate(R.id.action_completeOrderFragment_to_addAddressFragment,bundle)
            dismiss()
        }


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }



    interface OnClickListener {
        fun onClick(type: Int)
    }


    interface OnClickAddress {
        fun onClickAddress(data: Content)
    }


    override fun onClickListener(data: Content) {
        onClick.onClickAddress(data)
        Log.e("eee address",data.region_id)
        dismiss()
    }


}