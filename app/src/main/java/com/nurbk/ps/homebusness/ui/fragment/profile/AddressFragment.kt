package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.AddressAdapter
import com.nurbk.ps.homebusness.databinding.FragmentAddressBinding
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.ui.viewmodel.profile.AddressViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.EDITADDRESS
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_address.toolbar
import timber.log.Timber

class AddressFragment : Fragment(), AddressAdapter.OnClickItem {

    private lateinit var mBinding: FragmentAddressBinding


    private val addressAdapter by lazy {
        AddressAdapter(ArrayList(), this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[AddressViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAddressBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        btnAddAddress.setOnClickListener {
            val bundle =Bundle()
            bundle.putParcelable(EDITADDRESS,null)
            findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment,bundle)
        }

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
                            addressAdapter.data.clear()
                            addressAdapter.data.addAll(data.data.data)
                            addressAdapter.notifyDataSetChanged()
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


        rcDataAddress.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = addressAdapter
        }
    }


    override fun onClickListener(data: Content) {
        val bundle =Bundle()
        bundle.putParcelable(EDITADDRESS,data)
        findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment,bundle)
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

}