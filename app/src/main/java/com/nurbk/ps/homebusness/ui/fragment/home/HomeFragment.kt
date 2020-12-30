package com.nurbk.ps.homebusness.ui.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.HomeMealAdapter
import com.nurbk.ps.homebusness.adapter.HomeStoreAdapter
import com.nurbk.ps.homebusness.adapter.HomeStoryAdapter
import com.nurbk.ps.homebusness.databinding.FragmentHomeBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.model.DataHome.SpecialMarket
import com.nurbk.ps.homebusness.model.DataHome.Stories
import com.nurbk.ps.homebusness.ui.viewmodel.cart.CartViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.home.HomeViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.home.NotificationViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.profile.ProfileViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.DETAILS_PRODUCT
import com.nurbk.ps.homebusness.util.Constant.DETAILS_STORE
import com.nurbk.ps.homebusness.util.Constant.MARKET_ID
import com.nurbk.ps.homebusness.util.Constant.SEARCH
import com.nurbk.ps.homebusness.util.Constant.TOKEN
import com.nurbk.ps.homebusness.util.Constant.USERNAME
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_design_home_story.view.*
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), HomeStoryAdapter.OnViewStory, HomeMealAdapter.OnClickListener {

    private lateinit var mBinding: FragmentHomeBinding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    private val viewModelProfile by lazy {
        ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
    }

    private val viewModelNotification by lazy {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    private val viewModelCart by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }
    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }

    private var builder : StoryView.Builder? = null

    private val adapterSpacialStores =
        HomeStoreAdapter(ArrayList(), 1, object : HomeStoreAdapter.OnClickListener {
            override fun onClickItem(data: SpecialMarket) {
                val bundle = Bundle()
                bundle.putString(DETAILS_STORE, data.id.toString())
                findNavController().navigate(
                    R.id.action_homeFragment_to_storeDetailsFragment,
                    bundle
                )

            }
        })
    private val adapterNewStores =
        HomeStoreAdapter(ArrayList(), 0, object : HomeStoreAdapter.OnClickListener {
            override fun onClickItem(data: SpecialMarket) {
                val bundle = Bundle()
                bundle.putString(DETAILS_STORE, data.id.toString())
                findNavController().navigate(
                    R.id.action_homeFragment_to_storeDetailsFragment,
                    bundle
                )

            }
        })
    private val adapterStories = HomeStoryAdapter(ArrayList(), this)

    private val adapterNewMeals =
        HomeMealAdapter(ArrayList(), 1, object : HomeMealAdapter.OnClickListener {
            override fun onClickItem(data: NewProduct) {
                val bundle = Bundle()
                bundle.putString(DETAILS_PRODUCT, data.id.toString())
                findNavController().navigate(
                    R.id.action_homeFragment_to_productDetailsFragment,
                    bundle
                )
            }
        })

    private val adapterSpacialMeals = HomeMealAdapter(ArrayList(), 0, this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handelDynamicLink()
    }

    private fun handelDynamicLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(requireActivity().intent)
            .addOnSuccessListener {
                if (it != null) {
                    val link = it.link
                    val id =
                        link!!.encodedQuery!!.subSequence(8, link.encodedQuery!!.length).toString()
                    val bundle = Bundle()
                    bundle.putString(DETAILS_STORE, id)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_storeDetailsFragment,
                        bundle
                    )
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.clFCM.setOnClickListener {
            if (getShare.getString(TOKEN, "")
                != null && getShare.getString(TOKEN, "").toString().isNotEmpty()
            ) {
                findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.singin),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        Constant.showDialog(requireActivity())

        mBinding.imgProfile.setOnClickListener {
            if (getShare.getString(TOKEN, "") != null && getShare.getString(TOKEN, "").toString()
                    .isNotEmpty()
            ) {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.singin),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.dataHomeLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Constant.dialog.hide()
                        Constant.dialog.dismiss()
                        if (data.status) {

                            adapterStories.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.stories)
                                }
                                this.notifyDataSetChanged()
                            }

                            //adapterSpacialStores
                            adapterSpacialStores.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.specialMarkets)
                                }
                                this.notifyDataSetChanged()
                            }

                            //adapterSpacialMeals
                            adapterSpacialMeals.apply {
                                this.data.apply {
                                    clear()
                                    addAll(
                                        data.data.specialProducts
                                    )
                                }
                                this.notifyDataSetChanged()
                            }

                            //adapterSpacialStores
                            adapterNewStores.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.newMarkets)
                                }
                                this.notifyDataSetChanged()
                            }

                            //adapterSpacialMeals
                            adapterNewMeals.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.newProducts)
                                }
                                this.notifyDataSetChanged()
                            }

                            //imgFirstBannar
                            banner(imgFirstBannar, data.data.upBanner.image, data.data.upBanner.url)

                            //imgSecondBannar
                            banner(
                                imgSecondBannar, data.data.middleBanner.image,
                                data.data.middleBanner.url
                            )

                            //imgThirdBannar
                            banner(
                                imgThirdBannar, data.data.middleBanner2.image,
                                data.data.middleBanner2.url
                            )

                            //imgFourthBannar
                            banner(
                                imgFourthBannar, data.data.middleBanner3.image,
                                data.data.middleBanner3.url
                            )

                            //imLastBannar
                            banner(
                                imLastBannar, data.data.downBanner.image,
                                data.data.downBanner.url
                            )


                        }
                    }
                }
                is Resource.Error -> {

                    it.message?.let { message ->

                    }
                }
                is Resource.Loading -> {

                }
            }
        })

        if (getShare.getString(TOKEN, "") == null && getShare.getString(TOKEN, "").toString()
                .isNotEmpty()
        ) {
            viewModelProfile.dataProfileLiveData.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        Timber.e(" onViewCreated->Resource.Success")
                        response.data?.let { data ->

                            if (data.status) {
                                Constant.setImage(
                                    requireContext(),
                                    data.data.avatar,
                                    imgProfile,
                                    R.drawable.store_placeholder
                                )

                            }
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            })
            viewModelNotification.getNotification()
            viewModelNotification.dataNotificationLiveData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        it.data?.let { data ->
                            if (data.status) {

                                mBinding.tvNtfNum.text = data.data.unRead.toString()
                                if (data.data.unRead > 0) {
                                    mBinding.cardNtf.visibility = View.VISIBLE
                                }
                                viewModelNotification.dataNotificationLiveData.value = null
                                viewModelNotification.data = null
                                viewModelNotification.page = 1
                            }
                        }
                    }
                    is Resource.Error -> {
                        it.message?.let { message ->
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            })
            viewModelCart.dataCartLiveData.observe(viewLifecycleOwner, Observer { response ->
                Timber.e(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.e(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                if (data.data != null) {
                                    getShare.edit()
                                        .putString(
                                            MARKET_ID,
                                            data.data.data!![0].marketId.toString()
                                        ).apply()
                                }
                            } else {
                                Timber.e("eee success ${data.code}")
                            }
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            })

        }
        tvAllNewMeals.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newProductFragment)
        }

        tvAllNewStores.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newStoreFragment)
        }

        tvAllSpacialMeals.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSpacialStoresFragment)
        }

        tvAllSpacialStores.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSpacialStoryFragment)
        }


        loadSpacialStores()
        loadStories()
        loadSpacialMeals()
        loadNewStores()
        loadNewMeals()

        mBinding.imgSearch.setOnClickListener {
            val searchQuery = mBinding.edSearch.text.toString()
            val bundle = Bundle()
            bundle.putString(SEARCH, searchQuery)
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment, bundle)
        }
    }

    private fun loadSpacialStores() {
        rcSpacialStores.apply {
            adapter = adapterSpacialStores
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun loadStories() {
        rcStories.apply {
            adapter = adapterStories
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun loadSpacialMeals() {
        rcSpacialMeals.apply {
            adapter = adapterSpacialMeals
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun loadNewStores() {
        rcNewStores.apply {
            adapter = adapterNewStores
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun loadNewMeals() {
        rcNewMeals.apply {
            adapter = adapterNewMeals
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }
    }

    private fun banner(imageView: ImageView, uriImage: String, uri: String) {
        imageView.apply {
            Constant.setImage(
                requireContext(),
                uriImage,
                imageView,
                R.drawable.ic_profile_default
            )
            setOnClickListener {
                val myIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(uri)
                )
                startActivity(myIntent)
            }

        }

    }

    override fun onView(data: Stories) {
        val currentStory = MyStory(
            data.image, Calendar.getInstance().time,
        )

        builder = StoryView.Builder(requireActivity().supportFragmentManager)
            .setStoriesList(arrayListOf(currentStory)) // Required
            .setStoryDuration(10000) // Default is 2000 Millis (2 Seconds)
            .setTitleText(data.marketName) // Default is Hidden
            .setTitleLogoUrl(data.marketImage) // Default is Hidden
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {

                }

                override fun onTitleIconClickListener(position: Int) {
                    val bundle = Bundle()
                    bundle.putString(DETAILS_STORE, data.id.toString())
                    findNavController().navigate(
                        R.id.action_homeFragment_to_storeDetailsFragment,
                        bundle
                    )
                }
            })
            .build() // Must be called before calling show method
        builder!!.show()
    }

    override fun onClickItem(data: NewProduct) {
        val bundle = Bundle()
        bundle.putString(DETAILS_PRODUCT, data.id.toString())
        findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
    }

    override fun onPause() {
        super.onPause()
        if (builder != null){
            builder!!.dismiss()
        }
    }
}