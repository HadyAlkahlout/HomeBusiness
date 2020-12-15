package com.nurbk.ps.homebusness.ui.fragment.profile

import android.Manifest
import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.FragmentEditProfileBinding
import com.nurbk.ps.homebusness.model.profile.UpdateProfile
import com.nurbk.ps.homebusness.ui.viewmodel.profile.ProfileViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.toolbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

class EditProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentEditProfileBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val mapimage = ArrayList<MultipartBody.Part>()
    private val REQUEST_IMAGE_CODE = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditProfileBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        btn_choose_image.setOnClickListener {
            permissionImage()
        }

        btn_save_profile.setOnClickListener {
            if (editTextTextPersonName.text.toString().isEmpty()) {
                mBinding.editTextTextPersonName.error = getString(R.string.errorMessage)
                mBinding.editTextTextPersonName.requestFocus()
                return@setOnClickListener
            }

            if (editTextTextPersonEmail.text.toString().isEmpty()) {
                mBinding.editTextTextPersonEmail.error = getString(R.string.errorMessage)
                mBinding.editTextTextPersonEmail.requestFocus()
                return@setOnClickListener
            }

            viewModel.updateProfileAccount(UpdateProfile(editTextTextPersonName.text.toString(),editTextTextPersonEmail.text.toString()))
        }


        viewModel.dataUpdateProfileLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        if (data.status) {
                            dialog.dismiss()
                            findNavController().navigateUp()
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                }
            }
        })


        viewModel.dataProfileLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        dialog.dismiss()

                        if (data.status) {
                            Glide.with(requireActivity()).load(data.data.avatar)
                                .into(tv_profile_img)
                            editTextTextPersonName.setText(data.data.name)
                            editTextTextPersonPhone.setText(data.data.mobile)
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                }
            }
        })



        viewModel.dataUpdateLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Snackbar.make(mBinding.root,requireActivity().getString(R.string.updateProfileSuccessfuly),Snackbar.LENGTH_SHORT).show()
                        dialog.dismiss()
                        if (data.status!!) {
                            Constant.getSharePref(requireContext()).edit().putString(Constant.USERNAME,editTextTextPersonName.text.toString()).apply()
                            Log.e("eee update", data.toString())
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Log.e("eee error image", response.message.toString())
                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {


            val file = data!!.data!!
            tv_profile_img.setImageURI(file)

            val imagefile = File(Constant.getRealPathFromURI(requireContext(), Uri.parse(file.toString())))
            val reqBody = RequestBody.create("avatar".toMediaTypeOrNull(), imagefile)
            val partImage: MultipartBody.Part =
                MultipartBody.Part.createFormData("avatar", imagefile.name, reqBody)

            viewModel.uploadNewAvater(partImage)

        }

    }


    private fun permissionImage() {
        Timber.d(" Add Image")
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            selectImage()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {

                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {

            }
            .check()
    }


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/png"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }


}