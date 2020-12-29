package com.nurbk.ps.homebusness.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.ImageSliderAdapter
import com.nurbk.ps.homebusness.databinding.FragmentProductDetailsBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.ui.fragment.dialog.CartDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.DETAILS_PRODUCT
import com.nurbk.ps.homebusness.util.Constant.MARKET_ID
import com.nurbk.ps.homebusness.util.Constant.toRequestBody
import com.nurbk.ps.homebusness.util.Resource
import okhttp3.RequestBody
import timber.log.Timber

class ProductDetailsFragment : Fragment(), CartDialogFragment.CartGoClick {

    private lateinit var mBinding: FragmentProductDetailsBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }

    private val sliderAdapter = ImageSliderAdapter(ArrayList())

    private var productId = ""

    private var counter = 1;

    private val map: MutableMap<String, RequestBody> = HashMap()

    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }
    private lateinit var marketId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProductDetailsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.viewPager2.adapter = sliderAdapter
        mBinding.wormDotsIndicator.setViewPager2(mBinding.viewPager2)

        productId = requireArguments().getString(DETAILS_PRODUCT, "")

        viewModel.getProductDetails(productId)
        viewModel.dataProductDetailsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Constant.dialog.hide()
                        if (data.status) {


                            sliderAdapter.apply {
                                this.data.clear()
                                this.data.addAll(data.data.images)
                                notifyDataSetChanged()
                            }

                            mBinding.txtName.text = data.data.title
                            mBinding.txtDec.text = data.data.note
                            mBinding.txtPrice.text = data.data.price.toInt().toString()
                            marketId = data.data.userId.toString()
                            mBinding.btnFav.setImageResource(

                                if (data.data.fav) {
                                    R.drawable.ic_icon_awesome_heart
                                } else {
                                    R.drawable.ic_baseline_favorite_border_24

                                }
                            )

                            mBinding.btnFav.setOnClickListener {
                                if (Constant.getSharePref(requireContext())
                                        .getString(
                                            Constant.TOKEN,
                                            ""
                                        ) == null || Constant.getSharePref(requireContext())
                                        .getString(
                                            Constant.TOKEN, ""
                                        ).toString()
                                        .isEmpty()
                                ) {
                                    Toast.makeText(
                                        requireContext(),
                                        requireContext().getString(R.string.singin),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@setOnClickListener
                                }
                                if (data.data.fav) {
                                    viewModel.deleteFav(ClassID(productId))
                                    mBinding.btnFav.setImageResource(
                                        R.drawable.ic_baseline_favorite_border_24
                                    )
                                } else {
                                    viewModel.addFav(ClassID(productId))
                                    mBinding.btnFav.setImageResource(
                                        R.drawable.ic_icon_awesome_heart
                                    )
                                }
                                data.data.fav = !data.data.fav
                            }


                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.hide()

                    it.message?.let { message ->

                    }
                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                }
            }
        })


        mBinding.btnIncres.setOnClickListener {
            counter++
            mBinding.txtCounter.text = counter.toString()
        }
        mBinding.btnMinus.setOnClickListener {
            if (counter > 1) {
                counter--
            }
            mBinding.txtCounter.text = counter.toString()

        }



        viewModel.statusLiveData.observe(viewLifecycleOwner,
            Observer { response ->
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
                            }
                            if (data.code == 121) {
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


        viewModel.deleteLiveData.observe(viewLifecycleOwner,
            Observer { response ->
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

        mBinding.btnAddToCart.setOnClickListener {

            if (getShare.getString(Constant.TOKEN, "")
                == null && getShare.getString(Constant.TOKEN, "").toString().isEmpty()
            ) {
                Snackbar.make(
                    mBinding.root,
                    getString(R.string.singin),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (getShare.getString(MARKET_ID, "")!!.toString().isEmpty()
                || getShare.getString(MARKET_ID, "")!!.toString() == marketId ||
                getShare.getString(MARKET_ID, "")!!.toString() == ""
            ) {
                map["cart[0][item_id]"] = toRequestBody(productId)
                map["cart[0][number]"] = toRequestBody(counter.toString())
                map["cart[0][note]"] = toRequestBody(mBinding.etxtMoreNotice.text.toString())

                viewModel.addCarts(map)
                getShare.edit().putString(MARKET_ID, marketId).apply()
            } else {
                CartDialogFragment(this).show(requireActivity().supportFragmentManager, "cart")
            }
        }

    }

    override fun onClickCart() {
        findNavController().navigate(R.id.action_productDetailsFragment_to_cartFragment)
    }


}