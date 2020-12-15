package com.nurbk.ps.homebusness.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.HomeMealAdapter
import com.nurbk.ps.homebusness.databinding.FragmentAllBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.ui.viewmodel.home.AllSpecialProductViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.OnScrollListener
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_all.*
import timber.log.Timber

class AllSpacialProductFragment : Fragment(),HomeMealAdapter.OnClickListener {


    private lateinit var mBinder: FragmentAllBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[AllSpecialProductViewModel::class.java]
    }

    private val newProductAdapter = HomeMealAdapter(ArrayList(), 3,this)
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinder = FragmentAllBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinder.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTitleAll.text = getString(R.string.spacial_meals)
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getDataSpecialProduct()
        Constant.showDialog(requireActivity())


        viewModel.dataSpecialProductViewModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        hideProgressBar()
                        if (data.status) {
                            onScrollListener.totalCount =  data.data.countTotal
                            newProductAdapter.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.data)
                                }
                                notifyDataSetChanged()
                            }

                        }
                    }
                }
                is Resource.Error -> {
                    Constant.showDialog(requireActivity())
                    hideProgressBar()

                    it.message?.let { message ->

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        })
        loadData()
    }


    private fun hideProgressBar() {
        Constant.dialog.hide()
        Timber.d("hideProgressBar")
        isLoading = false
    }

    private fun showProgressBar() {
        Timber.d(" showProgressBar")
        isLoading = true
    }


    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getDataSpecialProduct()
        isScrolling = false
    }

    private fun loadData() {
        mBinder.rcDataAll.apply {
            adapter = newProductAdapter
            layoutManager =
                GridLayoutManager(requireContext(),3)
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onClickItem(data: NewProduct) {
        val bundle = Bundle()
        bundle.putString(Constant.DETAILS_PRODUCT, data.id.toString())
        findNavController().navigate(R.id.action_allSpacialStoresFragment_to_productDetailsFragment,bundle)
    }
}