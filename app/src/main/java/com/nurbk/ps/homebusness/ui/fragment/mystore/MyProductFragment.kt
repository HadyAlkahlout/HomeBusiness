package com.nurbk.ps.homebusness.ui.fragment.mystore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.MyProductAdapter
import com.nurbk.ps.homebusness.databinding.FragmentMyProductBinding
import com.nurbk.ps.homebusness.model.myproduct.Content
import com.nurbk.ps.homebusness.model.myproduct.UpdateStatus
import com.nurbk.ps.homebusness.ui.fragment.dialog.TamezMarketFragment
import com.nurbk.ps.homebusness.ui.viewmodel.store.MyStoreViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_my_product.*
import timber.log.Timber


class MyProductFragment : Fragment(), MyProductAdapter.OnClickItem,
    TamezMarketFragment.GoFragmentMessage {

    private val adapter_product by lazy {
        MyProductAdapter(requireActivity(), ArrayList(), this, 1)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MyStoreViewModel::class.java]
    }


    private lateinit var mBinding: FragmentMyProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyProductBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.dataMyProductLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        if (data.data != null) {
                            Log.e("tttttt", "${data.data.data.size}")
                            adapter_product.data.clear()
                            adapter_product.data.addAll(data.data.data)
                            adapter_product.notifyDataSetChanged()
                            Constant.dialog.dismiss()
                            Log.e("eee order2", data.data.toString())
                        } else {
                            Constant.dialog.dismiss()
                            Log.e("eee order2", "null no product yet")
                        }
                        if (data.status) {
                            Timber.e("eee success ${data.data}")
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })



        viewModel.dataChangeStatusLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()
                        Log.e("eee order2", data.toString())
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })

        list_myProduct.adapter = adapter_product

    }

    override fun onClickListener(order: Content, type: Int) {
        when (type) {
            1 -> {
                Log.e("eee", order.toString())
                findNavController().navigate(
                    R.id.action_myProductFragment_to_addProductFragment,
                    Bundle().apply {
                        putString("type", "update")
                        putParcelable("updateProduct", order)
                    })
            }
            2 -> {
                if (type == 0) {
                    viewModel.ChangeProductStatus(UpdateStatus(order.id, 1))
                    Log.e("eee type 0", order.status.toString())
                } else {
                    viewModel.ChangeProductStatus(UpdateStatus(order.id, 0))
                    Log.e("eee type 1", order.status.toString())
                }
            }
            3 -> {
                requireActivity().intent.putExtra("product_id", order.id)
                TamezMarketFragment(this, 1).show(childFragmentManager, "")
            }
        }
    }

    override fun onClick(type: Boolean) {
    }


}