package com.nurbk.ps.homebusness.ui.fragment.profile

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentProfileBinding
import com.nurbk.ps.homebusness.model.profile.Data
import com.nurbk.ps.homebusness.ui.activity.AuthActivity
import com.nurbk.ps.homebusness.ui.fragment.dialog.TamezMarketFragment
import com.nurbk.ps.homebusness.ui.viewmodel.profile.ProfileViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.DATA_PROFILE
import com.nurbk.ps.homebusness.util.Constant.TYPE
import com.nurbk.ps.homebusness.util.Constant.setUpStatusBar
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

class ProfileFragment : Fragment(),TamezMarketFragment.GoFragmentMessage {

    private lateinit var dataProfile: Data
    private lateinit var mBinding: FragmentProfileBinding
    private val viewModelProfile by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        mBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        btnAddress.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addressFragment)
        }

        btnMyOrder.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myOrderFragment)

        }

        btnFav.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_favoriteProfileFragment)
        }

        btnSetting.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_settingFragment)
        }

        btnQuestion.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_questionFragment)

        }



        btnPrivecy.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(TYPE, 0)
            }
            findNavController().navigate(R.id.action_profileFragment_to_privacyFragment, bundle)
        }


        btnCondation.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(TYPE, 1)
            }
            findNavController().navigate(R.id.action_profileFragment_to_privacyFragment, bundle)
        }


        storeProfile.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(DATA_PROFILE, dataProfile)
            }
            findNavController().navigate(R.id.action_profileFragment_to_myStoreFragment,bundle)
        }

      /*  editStore.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(DATA_PROFILE, dataProfile)
            }
            findNavController().navigate(R.id.action_profileFragment_to_editStoreFragment, bundle)
        }*/

        btnUpgrade.setOnClickListener {
            TamezMarketFragment(this,2).show(childFragmentManager, "")
        }

        viewModelProfile.dataProfileLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()

                        if (data.status) {

                            dataProfile = data.data
                            if (data.data.market!!) {
                             //   editStore.visibility = View.VISIBLE
                              //  view9.visibility = View.VISIBLE
                             editProfile.visibility = View.GONE
                                btnUpgrade.visibility = View.GONE
                                storeProfile.visibility = View.VISIBLE
                            }
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


        btn_signout.setOnClickListener {
            Constant.getSharePref(requireContext()).edit().clear().apply()
            requireActivity().startActivity(Intent(requireContext(),AuthActivity::class.java))
            requireActivity().finish()
        }

    }

    override fun onClick(type: Boolean) {
        if (type){
            findNavController().navigate(R.id.action_profileFragment_to_editStoreFragment)
        }
    }



}