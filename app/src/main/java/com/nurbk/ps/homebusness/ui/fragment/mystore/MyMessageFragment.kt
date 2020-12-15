package com.nurbk.ps.homebusness.ui.fragment.mystore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.MyMessageAdapter
import com.nurbk.ps.homebusness.adapter.MyProductAdapter
import com.nurbk.ps.homebusness.databinding.FragmentCartBinding
import com.nurbk.ps.homebusness.databinding.FragmentMyMessageBinding
import com.nurbk.ps.homebusness.model.cart.City
import com.nurbk.ps.homebusness.model.cart.Region
import com.nurbk.ps.homebusness.model.mymessage.Content
import com.nurbk.ps.homebusness.ui.viewmodel.cart.CartViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.store.MyMessageViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.OnScrollListener
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_my_message.*
import timber.log.Timber

class MyMessageFragment : Fragment(),MyMessageAdapter.OnClickListener {

    private lateinit var mBinding: FragmentMyMessageBinding

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    private val adapter_Message by lazy {
        MyMessageAdapter(requireActivity(), ArrayList(), this)
    }


    private val viewModel by lazy {
        ViewModelProvider(this)[MyMessageViewModel::class.java]
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyMessageBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.dataMyMessageLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->

                        if (data.status) {
                            if (data.data == null) {
                                Log.e("eee message ", "null")
                                Toast.makeText(requireContext(),"not data",Toast.LENGTH_SHORT).show()
                                Constant.dialog.dismiss()
                            }else {
                                onScrollListener.totalCount =  data.data.countTotal
                                adapter_Message.data.clear()
                                adapter_Message.data.addAll(response.data.data!!.data)
                                adapter_Message.notifyDataSetChanged()
                                Constant.dialog.dismiss()
                                Log.e("eee message ", data.data!!.data.toString())
                            }
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                }
            }
        })


        list_myMessages.apply {
            adapter = adapter_Message
            layoutManager =
                LinearLayoutManager(requireContext())
            addOnScrollListener(onScrollListener)
        }


        super.onViewCreated(view, savedInstanceState)
    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getMyMessage()
        isScrolling = false
    }

    override fun onClickItem(data: Content, position: Int) {

    }

}