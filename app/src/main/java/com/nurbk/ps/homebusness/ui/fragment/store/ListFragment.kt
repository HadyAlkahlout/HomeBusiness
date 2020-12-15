package com.nurbk.ps.homebusness.ui.fragment.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.ListStoresAdapter
import com.nurbk.ps.homebusness.databinding.FragmentStoreListBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.StoreDetails.Category
import com.nurbk.ps.homebusness.model.StoreDetails.Product
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import okhttp3.RequestBody
import timber.log.Timber

class ListFragment(val data: ArrayList<Category>, val idMarket: String) : Fragment(),
    ListStoresAdapter.OnClickListener {

    private lateinit var mBinding: FragmentStoreListBinding

    val mAdapter = ListStoresAdapter(data, this)

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }
    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }
    private val map: MutableMap<String, RequestBody> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStoreListBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMost()


        viewModel.statusLiveData.observe(viewLifecycleOwner,
                Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.addedFavorites),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                viewModel.statusLiveData.value = null
                            } else if (data.code == 121) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.alreadyAdded),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
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

        viewModel.deleteLiveData.observe(viewLifecycleOwner,
            Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    "تم الازالة من المضلة",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.deleteLiveData.value = null
                            } else if (data.code == 121) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.alreadyAdded),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
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
        viewModel.addCartLiveData.observe(viewLifecycleOwner,
            Observer { response ->
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
                                viewModel.addCartLiveData.value = null
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
    }


    private fun loadMost() {
        mBinding.rcDataList.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }


    override fun onClickItem(data: Product, type: Int) {
        when (type) {
            1 -> {
                val bundle = Bundle()
                bundle.putString(Constant.DETAILS_PRODUCT, data.id.toString())
                findNavController().navigate(
                    R.id.action_storeDetailsFragment_to_productDetailsFragment,
                    bundle
                )
            }
            2 -> {

                if (getShare.getString(Constant.TOKEN, "") == null || getShare.getString(
                        Constant.TOKEN, "").toString().isEmpty()
                ) {
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.singin),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return
                }
                if (getShare.getString(Constant.MARKET_ID, "")!!.toString().isEmpty()
                    || getShare.getString(Constant.MARKET_ID, "")!!.toString() == idMarket
                ) {
                    map["cart[0][item_id]"] = Constant.toRequestBody(data.id.toString())
                    map["cart[0][number]"] = Constant.toRequestBody("1")
                    map["cart[0][note]"] =
                        Constant.toRequestBody("")

                    viewModel.addCarts(map)
                }else{
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.buyStore),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun addFav(data: Product, position: Int) {
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
            return
        } else {
            if (data.fav) {
                viewModel.deleteFav(ClassID(data.id.toString()))

            } else {
                viewModel.addFav(ClassID(data.id.toString()))
            }

            mAdapter.notifyItemChanged(position)
            data.fav = !data.fav
        }
    }
}