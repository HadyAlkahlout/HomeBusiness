package com.nurbk.ps.homebusness.ui.fragment.mystore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.AddImageAdapter
import com.nurbk.ps.homebusness.adapter.SpinnerRegionAdapter
import com.nurbk.ps.homebusness.databinding.FragmentAddProductBinding
import com.nurbk.ps.homebusness.model.ProductDetails.Image
import com.nurbk.ps.homebusness.model.myproduct.Content
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.profile.UpdateMarketViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.store.AddProductViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.REQUEST_IMAGE_CODE
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.getRealPathFromURI
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Constant.toRequestBody
import com.nurbk.ps.homebusness.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

class AddProductFragment : Fragment(), AddImageAdapter.CancelClick {

    private lateinit var mBinding: FragmentAddProductBinding
    private val adapterImage = AddImageAdapter(ArrayList(), this)
    private val map: MutableMap<String, RequestBody> = HashMap()
    private val mapimage = ArrayList<MultipartBody.Part>()

    private val viewModel by lazy {
        ViewModelProvider(this)[AddProductViewModel::class.java]
    }
    private val viewModelCate by lazy {
        ViewModelProvider(requireActivity())[UpdateMarketViewModel::class.java]
    }
    val update by lazy { requireArguments() }
    private val updateImage = ArrayList<String>()

    private lateinit var data: Content

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAddProductBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    private val viewModelDetails by lazy {
        ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }

    private val categorySub1Adapter = SpinnerRegionAdapter(ArrayList<Any>(), 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.dataStatusLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            dialog.hide()
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    "تم الإضافة بنجاح",
                                    Snackbar.LENGTH_SHORT
                                ).show()
//                                viewModelCategories.getCategories().also {
                                  findNavController().navigateUp()
//                                    findNavController().navigateUp()
  //                              }
                            }

                        }
                    }
                    is Resource.Error -> {
                        dialog.hide()

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                        showDialog(requireActivity())
                    }
                }
            })


        mBinding.btnAddImage.setOnClickListener {
            if (adapterImage.data.size < 4)
                Constant.permissionImger(requireContext()) {
                    selectImage()
                } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.done),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        mBinding.spineerEditMain.adapter = categorySub1Adapter
        viewModelCate.dataSubCategoryLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Constant.dialog.hide()

                        if (data.status) {

                            categorySub1Adapter.dataSource = data.data
                            categorySub1Adapter.notifyDataSetChanged()

                        }
                    }
                }
                is Resource.Error -> {
                    Constant.showDialog(requireActivity())
                    Constant.dialog.hide()
                    it.message?.let { message ->

                    }
                }
                is Resource.Loading -> {

                }
            }
        })

        if (update.getString("type", "") == "update") {

            data = update.getParcelable<Content>("updateProduct")!!

            map["id"] = toRequestBody(data.id.toString())
            mBinding.productName.setText(data.title)
            mBinding.etxtCount.setText("45")
            mBinding.etxtPrice.setText(data.price)
            mBinding.etxtProductDescription.setText(data.note)

            for ((i, cat) in categorySub1Adapter.dataSource.withIndex()) {
                val category = (cat as com.nurbk.ps.homebusness.model.DataCategories.Data)
                if (category.id.toString() == data.categoryId) {
                    mBinding.spineerEditMain.setSelection(i)
                    break
                }
            }

            viewModelDetails.getProductDetails(data.id.toString())
            viewModelDetails.dataProductDetailsLiveData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        it.data?.let { data ->
                            dialog.hide()
                            if (data.status) {

                                adapterImage.data.addAll(data.data.images)
                                adapterImage.notifyDataSetChanged()


                            }
                        }
                    }
                    is Resource.Error -> {
                        dialog.hide()

                        it.message?.let { message ->

                        }
                    }
                    is Resource.Loading -> {
                        showDialog(requireActivity())
                    }
                }
            })


            mBinding.btnSendCase.setOnClickListener {
                uploadData(0)
            }

        } else {
            mBinding.btnSendCase.setOnClickListener {
                uploadData(1)
            }
        }

        loadRecyclerView()
    }


    private fun loadRecyclerView() {
        mBinding.rcDataImageAddCase.apply {
            adapter = adapterImage
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun cancelClick(item: Image, i: Int) {
        if (adapterImage.data.size == 0)
            mBinding.rcDataImageAddCase.visibility = View.GONE

        adapterImage.data.remove(item)
        adapterImage.notifyDataSetChanged()


        if (update.getString("type", "") == "update") {
            viewModel.deleteImage(item.id.toString())

        }

    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/png"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {


            val file = data!!.data!!
            adapterImage.data.add(Image(0, file.toString()))
            adapterImage.notifyDataSetChanged()


            updateImage.add(file.toString())
            mBinding.rcDataImageAddCase.visibility = View.VISIBLE

        }

    }


    private fun uploadData(i: Int) {
        val name = mBinding.productName.text.toString()
        val count = mBinding.etxtCount.text.toString()
        val category =
            mBinding.spineerEditMain.selectedItem as com.nurbk.ps.homebusness.model.DataCategories.Data
        val price = mBinding.etxtPrice.text.toString()
        val dec = mBinding.etxtProductDescription.text.toString()

        if (name.isEmpty()) {
            mBinding.productName.error = getString(R.string.errorMessage)
            mBinding.productName.requestFocus()
            return
        }

        if (count.isEmpty()) {
            mBinding.etxtCount.error = getString(R.string.errorMessage)
            mBinding.etxtCount.requestFocus()
            return
        }
        if (price.isEmpty()) {
            mBinding.etxtCount.error = getString(R.string.errorMessage)
            mBinding.etxtCount.requestFocus()
            return
        }

        if (update.getString("type", "") != "update")
            for ((i, image) in adapterImage.data.withIndex()) {
                val imagefile = File(getRealPathFromURI(requireContext(), Uri.parse(image.image)))
                val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imagefile)
                val partImage: MultipartBody.Part =
                    MultipartBody.Part.createFormData("images[$i]", imagefile.name, reqBody)
                mapimage.add(partImage)
            }
        else {
            if (updateImage.isNotEmpty()) {
                for ((i, image) in updateImage.withIndex()) {
                    val imagefile = File(getRealPathFromURI(requireContext(), Uri.parse(image)))
                    val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imagefile)
                    val partImage: MultipartBody.Part =
                        MultipartBody.Part.createFormData("images[$i]", imagefile.name, reqBody)
                    mapimage.add(partImage)
                }
            }
        }


        map["title"] = toRequestBody(name)
        map["note"] = toRequestBody(dec)
        map["cat_id"] = toRequestBody(category.id.toString())
        map["price"] = toRequestBody(price)
        map["quantity"] = toRequestBody(count)

        viewModel.addProducts(map, mapimage, i)

    }
}