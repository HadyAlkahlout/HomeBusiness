package com.nurbk.ps.homebusness.ui.fragment.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import kotlinx.android.synthetic.main.dialog_rating.view.*
import kotlinx.android.synthetic.main.dialog_store.view.*
import kotlinx.android.synthetic.main.dialog_update.view.*
import kotlinx.android.synthetic.main.dialog_update.view.btnUpdate


class RatingDialogFragment(val onGo: GoFragmentMessage) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_rating, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)
        requireDialog().setCancelable(false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.btnAddRating.setOnClickListener {
            val commit = view.txtCommitRating.text.toString()
            val rating = view.ratingBar.rating
            if (commit.isEmpty()) {
                view.txtCommitRating.error = getString(R.string.empty_felid)
                view.txtCommitRating.requestFocus(
                )
                return@setOnClickListener
            }
            if (rating == 0.0f) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.ratings),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }


            onGo.onClickGo(rating, commit)
            dismiss()
        }

    }

    interface GoFragmentMessage {
        fun onClickGo(float: Float, commit: String)
    }




    

}