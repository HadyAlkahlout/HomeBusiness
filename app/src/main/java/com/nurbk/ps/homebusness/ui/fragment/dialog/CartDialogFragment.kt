package com.nurbk.ps.homebusness.ui.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nurbk.ps.homebusness.R
import kotlinx.android.synthetic.main.dialog_cart.view.*
import kotlinx.android.synthetic.main.dialog_problem.view.*
import kotlinx.android.synthetic.main.dialog_problem.view.btnProblem

class CartDialogFragment(val onGo: CartGoClick) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_cart, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
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
        view.btnCart.setOnClickListener {
            onGo.onClickCart()
            dialog!!.dismiss()
        }
    }

    interface CartGoClick {
        fun onClickCart()
    }

}