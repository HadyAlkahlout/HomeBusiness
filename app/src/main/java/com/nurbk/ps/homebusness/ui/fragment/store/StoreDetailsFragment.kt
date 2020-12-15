package com.nurbk.ps.homebusness.ui.fragment.store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.ViewPagerHome
import com.nurbk.ps.homebusness.databinding.FragmentDetailsStoreBinding
import com.nurbk.ps.homebusness.model.StoreDetails.StoreDetails
import com.nurbk.ps.homebusness.ui.activity.MainActivity
import com.nurbk.ps.homebusness.ui.fragment.dialog.SettingDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.home.StoreDetailsViewModel
import com.nurbk.ps.homebusness.util.AppBarStateChangeListener
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.DETAILS_STORE
import com.nurbk.ps.homebusness.util.Constant.MARKET_ID
import com.nurbk.ps.homebusness.util.Constant.setUpStatusBar
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_details_store.*
import timber.log.Timber


@Suppress("UNREACHABLE_CODE", "NAME_SHADOWING")
class StoreDetailsFragment : Fragment(), SettingDialogFragment.OnClickListener {

    private lateinit var mBinding: FragmentDetailsStoreBinding
    private val viewAdapter by lazy {
        ViewPagerHome(childFragmentManager)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[StoreDetailsViewModel::class.java]
    }
    private lateinit var details: StoreDetails
    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }

    private var storeId = ""
    private var nameStore = ""
    private var detailsStore = ""
    private var tel = ""
    private var idMarket = ""
    private lateinit var menu: Menu
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDetailsStoreBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpStatusBar(requireActivity(), 2)

        (requireActivity() as MainActivity).setSupportActionBar(toolbar)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val ratingsFragment = RatingsFragment(ArrayList(), idMarket)
        val mostWantedFragment = MostWantedFragment(ArrayList(), idMarket)
        val listFragment = ListFragment(ArrayList(), idMarket)





        storeId = requireArguments().getString(DETAILS_STORE, "")



        ratingStore.isEnabled = false

        viewModel.getStoreDetails(storeId).also {
            viewModel.dataStoreDetailsLiveData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        it.data?.let { data ->
                            Constant.dialog.hide()
                            if (data.status) {
                                details = data
                                idMarket = data.data.info.id.toString()
                                tel = data.data.info.mobile
                                nameStore = data.data.info.name
                                detailsStore = data.data.info.note
                                txtNameStore.text = nameStore
                                txtDesStore.text = data.data.info.note
                                ratingStore.rating = data.data.info.rate.toFloat()

                                val minOrder =
                                    resources.getString(R.string.minOrder, data.data.info.minOrder)

                                txtLetestStore.text = minOrder

                                Constant.setImage(
                                    requireContext(),
                                    data.data.info.image,
                                    imageStore,
                                    R.drawable.store_placeholder
                                )


                                //mostWantedFragment
                                mostWantedFragment.apply {
                                    this.data.apply {
                                        clear()
                                        addAll(data.data.mostSells)
                                        mostWantedFragment.mostAdapter.notifyDataSetChanged()
                                    }

                                }
                                //ratingsFragment
                                ratingsFragment.apply {
                                    this.data.apply {
                                        clear()
                                        addAll(data.data.reviews)
                                        ratingsFragment.mostAdapter.notifyDataSetChanged()
                                    }
                                }
                                //ratingsFragment
                                listFragment.apply {
                                    this.data.apply {
                                        clear()
                                        addAll(data.data.categories)
                                        listFragment.mAdapter.notifyDataSetChanged()
                                    }
                                }
                                setHasOptionsMenu(true)

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
            }).also {


                if (viewAdapter.lf.size == 0) {
                    viewAdapter.addFragment(listFragment, getString(R.string.list))
                    viewAdapter.addFragment(mostWantedFragment, getString(R.string.most_wanted))
                    viewAdapter.addFragment(ratingsFragment, getString(R.string.ratings))
                }
                mBinding.viewPager.adapter = viewAdapter
                mBinding.tabs.setupWithViewPager(mBinding.viewPager)

            }

        }

        viewModel.addFollowLiveData.observe(viewLifecycleOwner,
            Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.follow),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                viewModel.addFollowLiveData.value = null
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


        viewModel.deleteFollowLiveData.observe(viewLifecycleOwner,
            Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.unfollow),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                viewModel.deleteFollowLiveData.value = null
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



        mBinding.appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when (state) {
                    State.EXPANDED -> {
                        titleStore.visibility = View.GONE

                    }
                    State.COLLAPSED -> {
                        titleStore.visibility = View.VISIBLE
                        titleStore.text = nameStore

                    }
                    State.IDLE -> {

                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.menu_store, menu)

//        ic_baseline_notifications_active_24
        this.menu = menu
        menu.getItem(1).icon = ContextCompat
            .getDrawable(
                requireContext(),
                if (details.data.info.follow)
                    R.drawable.ic_baseline_notifications_active_24 else R.drawable.ic_bell
            )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            return false
        }
        when (item.itemId) {
            R.id.share -> {

                val dynamicLink = Firebase.dynamicLinks.shortLinkAsync { // or Firebase.dynamicLinks.shortLinkAsync
                    link = Uri.parse("https://www.albawabaa.page.link/HB?storeId=$idMarket")
                    domainUriPrefix = "https://homebusness.page.link"
                    androidParameters("com.nurbk.ps.homebusness"){}
                    iosParameters("com.Raiyansoft.HomeBusiness") {
                        appStoreId = "1534480288"
                    }
                    socialMetaTagParameters {
                        title = nameStore
                        description = detailsStore
                    }
                }.addOnSuccessListener {
                    Log.e("hdhd", "shareClick: ${it.shortLink}" )
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    val shareMessage = "\nLet me recommend you this Property\n\n${it.shortLink}"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                }

                return true
            }
            R.id.ball -> {
                if (details.data.info.follow) {
                    viewModel.deleteFollowStore(storeId)
                } else {
                    viewModel.addStoreFollow(storeId)

                }
                menu.getItem(1).icon = ContextCompat
                    .getDrawable(
                        requireContext(),
                        if (details.data.info.follow)
                            R.drawable.ic_bell else
                            R.drawable.ic_baseline_notifications_active_24
                    )
                details.data.info.follow = !details.data.info.follow

                return true
            }
            R.id.mask -> {
                SettingDialogFragment(
                    tel,
                    details.data.info.allowCall,
                    details.data.info.allowCall,
                    this
                ).show(requireActivity().supportFragmentManager, "")
                return true

            }
        }
        return super.onOptionsItemSelected(item)

    }


    override fun onClick(type: Int) {
        Constant.dialog.dismiss()

        when (type) {
            1 -> {

                val bundle = Bundle()
                bundle.putString(Constant.CALL, "call")
                bundle.putString(MARKET_ID, idMarket)
                findNavController().navigate(
                    R.id.action_storeDetailsFragment_to_callFragment,
                    bundle
                )

            }
            2 -> {
                val bundle = Bundle()
                bundle.putString(Constant.CALL, "callus")
                findNavController().navigate(
                    R.id.action_storeDetailsFragment_to_callFragment,
                    bundle
                )
            }
            3 -> {
                val sendIntent = Intent(Intent.ACTION_VIEW)
                val data = "content://com.android.contacts/data/$tel"
                val type = "vnd.android.cursor.item/vnd.com.whatsapp.profile"
                sendIntent.setDataAndType(Uri.parse(data), type)
                sendIntent.setPackage("com.whatsapp")
                if (sendIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(sendIntent)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.message), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
