package com.nurbk.ps.homebusness.ui.fragment.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.ReviewStoresAdapter
import com.nurbk.ps.homebusness.databinding.FragmentStoreMostBinding
import com.nurbk.ps.homebusness.databinding.FragmentStoreReviewBinding
import com.nurbk.ps.homebusness.model.StoreDetails.Review
import com.nurbk.ps.homebusness.ui.fragment.dialog.RatingDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.home.StoreDetailsViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.toRequestBody
import com.nurbk.ps.homebusness.util.Resource
import okhttp3.RequestBody
import timber.log.Timber

class RatingsFragment(val data: ArrayList<Review>, var id: String) : Fragment(),
    ReviewStoresAdapter.OnClickListener, RatingDialogFragment.GoFragmentMessage {

    private lateinit var mBinding: FragmentStoreReviewBinding

    val mostAdapter = ReviewStoresAdapter(data, this)
    private val map: MutableMap<String, RequestBody> = HashMap()
    private val viewModel by lazy {
        ViewModelProvider(this)[StoreDetailsViewModel::class.java]
    }
    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStoreReviewBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMost()

        viewModel.addRateLiveData.observe(viewLifecycleOwner,
            { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.addedCart),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                viewModel.addRateLiveData.value = null
                            }

                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })

        mBinding.btnAddRate.setOnClickListener {
            if (getShare.getString(Constant.TOKEN, "") == null || getShare.getString(
                    Constant.TOKEN, ""
                ).toString()
                    .isEmpty()
            ) {
                Snackbar.make(
                    mBinding.root,
                    getString(R.string.singin),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            RatingDialogFragment(this).show(requireActivity().supportFragmentManager, "")

        }
    }


    private fun loadMost() {
        mBinding.rcDataReview.apply {
            setHasFixedSize(true)
            adapter = mostAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


    }

    override fun onClickGo(float: Float, commit: String) {
        map["cart[0][item_id]"] = toRequestBody(id)
        map["cart[0][number]"] = toRequestBody(float.toString())
        map["cart[0][note]"] = toRequestBody(commit)
        viewModel.addRates(map)
    }

    override fun onClickItem(data: Review) {


    }
}