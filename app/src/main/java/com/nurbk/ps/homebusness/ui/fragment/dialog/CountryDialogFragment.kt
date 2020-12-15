package com.nurbk.ps.homebusness.ui.fragment.dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nurbk.ps.homebusness.adapter.MyExpandableClass
import com.nurbk.ps.homebusness.databinding.DialogCountryBinding
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.ui.viewmodel.auth.SplashViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.dialog_country.*
import timber.log.Timber


class CountryDialogFragment(val onClick: OnClickCountry,val type: Int) : DialogFragment(),
    MyExpandableClass.onClick {

    private lateinit var mBinding: DialogCountryBinding

    val header = ArrayList<Data>()
    val body = ArrayList<List<Region>>()
    private val mAdapter by lazy {
        MyExpandableClass(
            requireContext(),
            expandableListView,
            header,
            body,
            this
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[SplashViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogCountryBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        if (type== 0) {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        viewModel.dataCityLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Log.e("ttttt", "${data.status}")
                        if (data.status) {
                            for (title in data.data) {
                                header.add(title)
                                body.add(title.regions!!)

                            }
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.showDialog(requireActivity())


                    it.message?.let { message ->
                    }
                }
                is Resource.Loading -> {


                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners() {
        getData()

    }


    interface OnClickListener {
        fun onClick(type: Int)
    }


    private fun getData() {

        expandableListView.setAdapter(
            mAdapter
        )
    }

    override fun onClickItem(data: Data, region: Region) {
        onClick.onClick(data, region)
        dismiss()
    }

    interface OnClickCountry {
        fun onClick(country: Data, region: Region)
    }
}