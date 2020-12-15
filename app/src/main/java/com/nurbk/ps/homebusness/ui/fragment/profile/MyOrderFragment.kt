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
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.OrderAdapter
import com.nurbk.ps.homebusness.databinding.FragmnetOrderBinding
import com.nurbk.ps.homebusness.model.myorder.Content
import com.nurbk.ps.homebusness.ui.viewmodel.profile.MyOrderViewModel
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragmnet_order.*
import kotlinx.android.synthetic.main.fragmnet_order.toolbar
import timber.log.Timber

class MyOrderFragment : Fragment(), OrderAdapter.OnClickItem {


    private val viewModel by lazy {
        ViewModelProvider(this)[MyOrderViewModel::class.java]
    }


    val adapterOrder = OrderAdapter(ArrayList(), this)

    private lateinit var mBinding: FragmnetOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmnetOrderBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.dataMyOrderLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        dialog.hide()

                        if (data.status) {
                            if (data.data.data.size != 0) {
                                item_order.visibility = View.GONE
                                btnStartOrder.visibility = View.GONE
                            } else {
                                item_order.visibility = View.VISIBLE
                                btnStartOrder.visibility = View.VISIBLE
                            }
                            adapterOrder.data.clear()
                            adapterOrder.data.addAll(data.data.data)
                            adapterOrder.notifyDataSetChanged()
                            Timber.e("eee success ${data.data.data}")
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.hide()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })


        btnStartOrder.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
        }




        rcDataOrder.apply {
            adapter = adapterOrder
        }

    }


    override fun onClickListener(order: Content) {

    }

}