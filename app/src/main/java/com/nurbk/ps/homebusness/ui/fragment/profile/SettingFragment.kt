package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.util.LogTime
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentSettingBinding
import com.nurbk.ps.homebusness.ui.activity.AuthActivity
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.NoteficationStatus
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {


    private lateinit var mBinding: FragmentSettingBinding

    private val share by lazy {
        Constant.getSharePref(requireActivity())
    }

    private val edit
            by lazy { Constant.editor(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSettingBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner_lng.setSelection(if (share.getString(Constant.LANG,"") == "ar"){
            0
        }else{
            1
        })

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        spinner_lng.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (id == 0.toLong()){
                    if (share.getString(Constant.LANG,"") != "ar") {
                        AuthActivity.setLanguage("ar", requireContext())
                        edit.putString(Constant.LANG, "ar").apply()
                        requireActivity().recreate()
                    }
                }else{
                    if (share.getString(Constant.LANG,"") != "en") {
                        AuthActivity.setLanguage("en", requireContext())
                        edit.putString(Constant.LANG, "en").apply()
                        requireActivity().recreate()
                    }
                }
            }
        }

        switch1.isChecked = share.getBoolean(NoteficationStatus,false)

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                edit.putBoolean(NoteficationStatus,true)
            }else{
                edit.putBoolean(NoteficationStatus,false)
            }
            edit.apply()
            return@setOnCheckedChangeListener
        }

    }
}