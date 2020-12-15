package com.nurbk.ps.homebusness.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentLoginBinding
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.model.user.RegisterUser
import com.nurbk.ps.homebusness.ui.activity.AuthActivity
import com.nurbk.ps.homebusness.ui.activity.MainActivity
import com.nurbk.ps.homebusness.ui.fragment.dialog.CountryDialogFragment
import com.nurbk.ps.homebusness.ui.fragment.dialog.SettingDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.auth.AuthViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.LANG
import com.nurbk.ps.homebusness.util.Constant.NAME
import com.nurbk.ps.homebusness.util.Constant.PHONE_NUMBER
import com.nurbk.ps.homebusness.util.Constant.START
import com.nurbk.ps.homebusness.util.Constant.TOKEN
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.editor
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import timber.log.Timber

class LoginFragment : Fragment(), CountryDialogFragment.OnClickCountry {

    private lateinit var mBinding: FragmentLoginBinding

    private val share by lazy {
        getSharePref(requireActivity())
    }

    private val edit
            by lazy { editor(requireActivity()) }

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthActivity.setLanguage(share.getString(LANG, "ar").toString(), requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.txtCountryView.setOnClickListener {
            CountryDialogFragment(this,0).show(requireActivity().supportFragmentManager, "")
        }


        viewModel.dataUserLiveData.observe(viewLifecycleOwner,
           Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Log.e("TtttttData", "${data.status}")
                            dialog.hide()
                            if (data.status) {
                                val bundel = Bundle()
                                bundel.putBoolean(START, true)
                                edit.putString(TOKEN, "Bearer " + data.data.token)
                                edit.apply()

                                findNavController()
                                    .navigate(R.id.action_loginFragment_to_verificationFragment2)

                            }
                        }
                    }
                    is Resource.Error -> {
                        dialog.hide()
                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showDialog(requireActivity())
                    }
                }
            })

        mBinding.btnLogin.setOnClickListener {
            val name = mBinding.edLoginName.text.toString()
            val phone = mBinding.edLoginPhone.text.toString()
            val country = mBinding.txtCountryView.text.toString()

            if (name.isEmpty()) {
                mBinding.edLoginName.error = getString(R.string.errorMessage)
                mBinding.edLoginName.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                mBinding.edLoginPhone.error = getString(R.string.errorMessage)
                mBinding.edLoginPhone.requestFocus()
                return@setOnClickListener
            }
            if (phone.length != 8) {
                mBinding.edLoginPhone.error = getString(R.string.errorMessage)
                mBinding.edLoginPhone.requestFocus()
                return@setOnClickListener
            }
            if (country.isEmpty()) {
                mBinding.txtCountryView.error = getString(R.string.errorMessage)
                mBinding.txtCountryView.requestFocus()
                return@setOnClickListener
            }

            edit.putString(NAME, name)
            edit.putString(PHONE_NUMBER, phone)
            edit.putString(Constant.USERNAME,name)
            edit.apply()


            viewModel.registerUsers(
                RegisterUser(
                    name,
                    "00965$phone",
                    "android",
                    "tokenMessage",
                    "00965",
                    countryId, regionId

                )
            ).also {
//                Timber.e("eee token $tokenMessage")
            }
//                }
//            }
//            findNavController().navigate(R.id.action_loginFragment_to_verificationFragment2)
        }


        mBinding.tvLoginSkip.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    MainActivity::class.java
                )
            )
        }
    }

    var regionId = ""
    var countryId = ""
    override fun onClick(data: Data, region: Region) {
        mBinding.txtCountryView.setText(region.title)
        countryId = data.id.toString()
        regionId = region.id.toString()
    }
}