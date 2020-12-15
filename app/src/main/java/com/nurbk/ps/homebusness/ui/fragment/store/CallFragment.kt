package com.nurbk.ps.homebusness.ui.fragment.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentCallUsBinding
import com.nurbk.ps.homebusness.model.contact.Contact
import com.nurbk.ps.homebusness.model.contact.ContactMarket
import com.nurbk.ps.homebusness.ui.viewmodel.ContactViewModel
import com.nurbk.ps.homebusness.util.Constant.CALL
import com.nurbk.ps.homebusness.util.Constant.MARKET_ID
import com.nurbk.ps.homebusness.util.Constant.NAME
import com.nurbk.ps.homebusness.util.Constant.PHONE_NUMBER
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_call_us.*
import timber.log.Timber

class CallFragment : Fragment() {

    private lateinit var mBinding: FragmentCallUsBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ContactViewModel::class.java]
    }
    private val share by lazy {
        getSharePref(requireContext())

    }
    private var type = 1
    private var marketId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCallUsBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val bundle = requireArguments()

        if (bundle.getString(CALL) == "call") {
            mBinding.line1.visibility = View.GONE
            mBinding.layoutSo.visibility = View.GONE
            marketId = bundle.getString(MARKET_ID, "")
            type = 0
        }

        mBinding.btnSendCase.setOnClickListener {
            val reason = txtEditDescription.text.toString()
            val message = txtEditEmail.text.toString()

            if (reason.isEmpty()) {
                txtEditDescription.error = getString(R.string.errorMessage)
                txtEditDescription.requestFocus()
                return@setOnClickListener
            }
            if (message.isEmpty()) {
                txtEditEmail.error = getString(R.string.errorMessage)
                txtEditEmail.requestFocus()
                return@setOnClickListener
            }

            viewModel.postContact(
                Contact(
                    share.getString(NAME, " ").toString(),
                    share.getString(PHONE_NUMBER, " ").toString(),
                    " ",
                    "reason: $reason \n message: $message"
                ), type,
                ContactMarket(marketId, reason, message)
            )
        }



        viewModel.contactLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->

                            if (data.status) {
                                txtEditDescription.text.clear()
                                txtEditEmail.text.clear()
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.sendMessage),
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
    }


}