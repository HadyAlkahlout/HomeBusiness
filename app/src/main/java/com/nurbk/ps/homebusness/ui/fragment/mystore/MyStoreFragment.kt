package com.nurbk.ps.homebusness.ui.fragment.mystore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.MyOrderAdapter
import com.nurbk.ps.homebusness.adapter.MyProductAdapter
import com.nurbk.ps.homebusness.databinding.FragmentMyStoreBinding
import com.nurbk.ps.homebusness.model.myorder.Content
import com.nurbk.ps.homebusness.ui.fragment.dialog.StoryDialogFragment
import com.nurbk.ps.homebusness.ui.fragment.dialog.TamezMarketFragment
import com.nurbk.ps.homebusness.ui.viewmodel.profile.UpdateMarketViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.store.MyStoreViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_my_store.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


class MyStoreFragment : Fragment(), MyOrderAdapter.OnClickItem,
    MyProductAdapter.OnClickItem,
    TamezMarketFragment.GoFragmentMessage, StoryDialogFragment.GoFragmentMessage {

    private lateinit var mBinding: FragmentMyStoreBinding


    private val adapter by lazy {
        MyOrderAdapter(ArrayList(), this)
    }

    private val adapter_product by lazy {
        MyProductAdapter(requireActivity(), ArrayList(), this, 0)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MyStoreViewModel::class.java]
    }
    private val viewModelCate by lazy {
        ViewModelProvider(requireActivity())[UpdateMarketViewModel::class.java]
    }

    private val data by lazy {
        requireArguments().getParcelable<com.nurbk.ps.homebusness.model.profile.Data>(
            Constant.DATA_PROFILE
        )!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyStoreBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        txtNameStore.text = data.name
        txtDesStore.text = data.note
        ratingStore.isEnabled = false
        ratingStore.rating = data.rate!!.toFloat()


        Constant.setImage(
            requireContext(),
            data.avatar,
            imageStore,
            R.drawable.ic_profile_default
        )


        btn_customer_message.setOnClickListener {
            findNavController().navigate(R.id.action_myStoreFragment_to_myMessageFragment)
        }


        btn_move_edit_store.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(Constant.DATA_PROFILE, data)
            }
            findNavController().navigate(R.id.action_myStoreFragment_to_editStoreFragment, bundle)
        }

        mBinding.btnShareStory.setOnClickListener {
            Constant.permissionImger(requireContext()) {
                selectImage()
            }
        }

        viewModelCate.getSubCategories(data.catId.toString())

        viewModel.dataMyOrderLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        adapter.data.clear()
                        adapter.data.addAll(data.data.data)
                        adapter.notifyDataSetChanged()
                        if (data.status) {
                            Log.e("eee order", data.data.toString())
                            Timber.e("eee success ${data.data.data}")
                        }
                    }
                }
                is Resource.Error -> {
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    Log.e("eee error", response.message.toString())
                }
            }
        })


        viewModel.dataMyProductLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        if (data.data != null) {
                            adapter_product.data.clear()
                            adapter_product.data.addAll(data.data.data)
                            adapter_product.notifyDataSetChanged()
                        }
                        if (data.status) {
                            Timber.e("eee success ${data.data}")
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                }
            }
        })



        btn_move_edit_products.setOnClickListener {
            findNavController().navigate(R.id.action_myStoreFragment_to_myProductFragment)

        }

        tv_tameez_store.setOnClickListener {
            if (data.market!!) {
                TamezMarketFragment(this, 0).show(childFragmentManager, "")
            } else {
                TamezMarketFragment(this, 2).show(childFragmentManager, "")
            }
        }

        list_my_product.adapter = adapter_product
        list_my_order.adapter = adapter

        btn_move_to_add_product.setOnClickListener {
            findNavController().navigate(
                R.id.action_myStoreFragment_to_addProductFragment,
                Bundle().apply {
                    putString("idCate", "${data.catId}")
                })

        }

        viewModel.dataStatusLiveData.observe(viewLifecycleOwner,
            Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Constant.dialog.hide()
                            Constant.dialog.dismiss()
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.share),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.dataStatusLiveData.value = null
                            }

                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        Constant.showDialog(requireActivity())

                    }
                }
            })

    }


    override fun onClickListener(
        order: com.nurbk.ps.homebusness.model.myproduct.Content,
        type: Int
    ) {

    }

    override fun onClickListener(order: Content) {

    }

    override fun onClick(type: Boolean) {

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constant.REQUEST_IMAGE_CODE)
    }

    private lateinit var file: Uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == Constant.REQUEST_IMAGE_CODE
        ) {

            val image = data!!.extras!!["data"] as Bitmap?

            file = getImageUri(requireContext(), image!!)
//            file = data?.data!!


            StoryDialogFragment(
                this, file
            ).show(requireActivity().supportFragmentManager, "")
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    override fun onClickGo() {

        val imagefile = File(Constant.getRealPathFromURI(requireContext(), file))
        val reqBody = RequestBody.create("image".toMediaTypeOrNull(), imagefile)
        val partImage: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imagefile.name, reqBody)
        viewModel.addStory(partImage)
    }
}