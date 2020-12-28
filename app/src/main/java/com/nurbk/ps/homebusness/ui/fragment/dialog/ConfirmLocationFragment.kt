package com.nurbk.ps.homebusness.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nurbk.ps.homebusness.R
import kotlinx.android.synthetic.main.dialog_confirm_location.view.*
import timber.log.Timber

class ConfirmLocationFragment(val onGo: GoFragmentMessage) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_confirm_location, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        view.btnYea.setOnClickListener {
            onGo.onClick(1)
            dismiss()
        }

        view.btnNo.setOnClickListener {
            dismiss()
        }
    }

    interface GoFragmentMessage {
        fun onClick(type: Int)
    }

}