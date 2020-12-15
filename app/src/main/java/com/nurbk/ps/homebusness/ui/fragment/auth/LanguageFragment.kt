package com.nurbk.ps.homebusness.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentLangugeBinding
import com.nurbk.ps.homebusness.ui.activity.MainActivity
import com.nurbk.ps.homebusness.util.Constant.LANG
import com.nurbk.ps.homebusness.util.Constant.LOGIN
import com.nurbk.ps.homebusness.util.Constant.editor
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Constant.setUpStatusBar

class LanguageFragment : Fragment() {


    private lateinit var mBinding: FragmentLangugeBinding
    private val share by lazy {
        getSharePref(requireContext())
    }
    private val edit by lazy {
        editor(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (share.getBoolean(LOGIN, false)) {
            startActivity(Intent(requireContext(), MainActivity::class.java)).also {
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentLangugeBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.btnArabic.setOnClickListener {
            edit.putString(LANG, "ar").apply()
            navLogin()
            requireActivity().recreate();

        }
        mBinding.btnEnglish.setOnClickListener {
            edit.putString(LANG, "en").apply()
            navLogin()
            requireActivity().recreate();

        }
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_languageFragment_to_loginFragment2)
    }


}