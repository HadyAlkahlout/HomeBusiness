package com.nurbk.ps.homebusness.ui.fragment.dialog


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.DialogSettingBinding
import com.nurbk.ps.homebusness.databinding.DialogStoreBinding
import com.nurbk.ps.homebusness.util.Constant.CALL
import com.nurbk.ps.homebusness.util.Constant.TELEPHONE
import kotlinx.android.synthetic.main.dialog_store.*


class SettingDialogFragment(
    var tel: String,
    var call: Int, var whats: Int,
    val onClick: OnClickListener
) : DialogFragment() {

    private lateinit var mBinding: DialogSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogSettingBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        if (call == 0) {
            mBinding.btnTel.visibility = View.GONE
        }
        if (whats == 0) {
            mBinding.btnWhats.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners() {

        mBinding.btnTel.setOnClickListener {
            dismiss()
            permission()
        }

        mBinding.btnWhats.setOnClickListener {
            dismiss()
            onClick.onClick(3)
        }

        mBinding.txtSendStore.setOnClickListener {
            dismiss()
            onClick.onClick(1)
        }

        mBinding.txtSEndMas.setOnClickListener {
            dismiss()
            onClick.onClick(2)
        }

    }

    interface OnClickListener {
        fun onClick(type: Int)
    }

    private fun permission() {
        Dexter.withContext(requireContext())
            .withPermission(
                Manifest.permission.CALL_PHONE
            )
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {

                }

                override fun onPermissionGranted(report: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel: $tel")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                }
            })
            .check()
    }


}