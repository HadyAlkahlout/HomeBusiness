package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentPrivacyBinding
import com.nurbk.ps.homebusness.ui.viewmodel.profile.PrivacyViewModel
import com.nurbk.ps.homebusness.util.Constant.TYPE
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_privacy.*
import timber.log.Timber

class PrivacyFragment : Fragment() {


    private lateinit var mBinding: FragmentPrivacyBinding

    private val arg by lazy {
        requireArguments().getInt(TYPE)
    }

    private val viewmodel by lazy {
    ViewModelProvider(requireActivity())[PrivacyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPrivacyBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (arg == 0){
            viewmodel.getConditions()
            toolbar_title.text = requireActivity().getString(R.string.condition)
            viewmodel.dataConditionsLiveData.observe(viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    Timber.d(" onViewCreated->viewModel")
                    when (response) {
                        is Resource.Success -> {
                            Timber.d(" onViewCreated->Resource.Success")
                            response.data?.let { data ->
                                txtPrivacy.text = data.data.conditions
                            }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            Timber.d("onViewCreated-> Resource.Loading")
                        }
                    }
                })
        }else if (arg == 1){
            viewmodel.getPrivacy()
            toolbar_title.text = requireActivity().getString(R.string.privacy_policy)
            viewmodel.dataPrivacyLiveData.observe(viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    Timber.d(" onViewCreated->viewModel")
                    when (response) {
                        is Resource.Success -> {
                            Timber.d(" onViewCreated->Resource.Success")
                            response.data?.let { data ->
                                txtPrivacy.text = data.data.privacy
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

}