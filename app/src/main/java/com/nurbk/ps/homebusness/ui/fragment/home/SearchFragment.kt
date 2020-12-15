package com.nurbk.ps.homebusness.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.HomeMealAdapter
import com.nurbk.ps.homebusness.databinding.FragmentSearchBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.ui.viewmodel.home.AllNewProductViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.OnScrollListener
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_all.*
import timber.log.Timber
import kotlin.math.log

class SearchFragment : Fragment(), HomeMealAdapter.OnClickListener {

    private lateinit var mBinding: FragmentSearchBinding
    private val newProductAdapter = HomeMealAdapter(ArrayList(), 4, this)
    private val viewModel by lazy {
        ViewModelProvider(this)[AllNewProductViewModel::class.java]
    }


    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        if (requireArguments().getString(Constant.SEARCH, "").isNotEmpty())
            viewModel.getAllProduct(requireArguments().getString(Constant.SEARCH, ""))
        else {
            mBinding.searchText.visibility = View.VISIBLE

        }
        viewModel.dataAllProductViewModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        hideProgressBar()
                        if (data.status) {
                            onScrollListener.totalCount = data.data.countTotal
                            newProductAdapter.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.data)
                                }
                                notifyDataSetChanged()
                            }

                            if (newProductAdapter.data.size == 0) {
                                mBinding.searchText.visibility = View.VISIBLE
                            } else {
                                mBinding.searchText.visibility = View.GONE
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

        mBinding.imgSearch.setOnClickListener {
            val searchQuery = mBinding.edSearch.text.toString()
            if (searchQuery.isNotEmpty()) {
                newProductAdapter.data.removeAll(newProductAdapter.data)
                newProductAdapter.notifyDataSetChanged()
                viewModel.dataAllProductViewModel.value = null
                viewModel.data = null
                viewModel.page = 1
                viewModel.getAllProduct(searchQuery)
            }

        }

        loadData()
    }


    private fun hideProgressBar() {
        Constant.dialog.hide()
        Timber.d("hideProgressBar")
        isLoading = false
    }

    private fun showProgressBar() {
        Constant.showDialog(requireActivity())

        Timber.d(" showProgressBar")
        isLoading = true
    }


    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getAllProduct("")
        isScrolling = false
    }

    private fun loadData() {
        mBinding.rcDataSearch.apply {
            adapter = newProductAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3)
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onClickItem(data: NewProduct) {

        val bundle = Bundle()
        bundle.putString(Constant.DETAILS_PRODUCT, data.id.toString())
        findNavController().navigate(
            R.id.action_searchFragment_to_productDetailsFragment,
            bundle
        )
    }
}