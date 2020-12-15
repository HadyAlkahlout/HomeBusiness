package com.nurbk.ps.homebusness.ui.fragment.dialog


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nurbk.ps.homebusness.R
import kotlinx.android.synthetic.main.dialog_store.view.*


class StoryDialogFragment(val onGo: GoFragmentMessage, val image: Uri) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_store, container, false)
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)
        requireDialog().setCancelable(false)

        view.imageStory.setImageURI(image)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners(view: View) {

        view.btnShare.setOnClickListener {
            onGo.onClickGo()
            dismiss()
        }

    }

    interface GoFragmentMessage {
        fun onClickGo()
    }

}