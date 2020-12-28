package com.nurbk.ps.homebusness.ui.fragment.profile

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.AddCityAdapter
import com.nurbk.ps.homebusness.adapter.SpinnerRegionAdapter
import com.nurbk.ps.homebusness.databinding.FragmentEditStoreBinding
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.model.delivery.DataX
import com.nurbk.ps.homebusness.ui.fragment.dialog.MapDialog
import com.nurbk.ps.homebusness.ui.viewmodel.auth.SplashViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.category.CategoriesViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.profile.UpdateMarketViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.DATA_PROFILE
import com.nurbk.ps.homebusness.util.Constant.REQUEST_IMAGE_CODE
import com.nurbk.ps.homebusness.util.Constant.getRealPathFromURI
import com.nurbk.ps.homebusness.util.Constant.permissionImger
import com.nurbk.ps.homebusness.util.Constant.toRequestBody
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_edit_store.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EditStoreFragment : Fragment(),
    AddCityAdapter.OnClickItemListener, MapDialog.OnClickSaveLocationMap {


    private lateinit var mBinding: FragmentEditStoreBinding
    private val map: MutableMap<String, RequestBody> = HashMap()


    private val viewModel by lazy {
        ViewModelProvider(this)[UpdateMarketViewModel::class.java]
    }
    private val cityAdapter = AddCityAdapter(ArrayList(), 1, this)
    private val regionAdapter = AddCityAdapter(ArrayList(), 0, this)

    private val viewModelRegion by lazy {
        ViewModelProvider(requireActivity())[SplashViewModel::class.java]
    }


    private val viewModelCategory by lazy {
        ViewModelProvider(requireActivity())[CategoriesViewModel::class.java]
    }

    private val spinnerAdapter = SpinnerRegionAdapter(ArrayList<Any>(), 1)
    private val regionsAdapter = SpinnerRegionAdapter(ArrayList<Any>(), 1)

    private val categoryAdapter = SpinnerRegionAdapter(ArrayList<Any>(), 0)
    private val categorySub1Adapter = SpinnerRegionAdapter(ArrayList<Any>(), 0)
    private val deliveryAdapter = SpinnerRegionAdapter(ArrayList<Any>(), 3)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelRegion.dataCityLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        if (data.status) {
                            spinnerAdapter.dataSource = data.data
                            spinnerAdapter.notifyDataSetChanged()

                            if (arguments != null) {
                                val dataArg =
                                    requireArguments().getParcelable<com.nurbk.ps.homebusness.model.profile.Data>(
                                        DATA_PROFILE
                                    )!!
                                for (city in data.data) {
                                    val dataCountry = city
                                    for (cites in dataArg.cities!!) {
                                        if (dataCountry.id == cites.cityId) {
                                            dataCountry.price = cites.price.toString()
                                            cityAdapter.data.add(dataCountry)
                                            cityAdapter.notifyDataSetChanged()
                                            break
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })



        viewModelRegion.dataFarRegionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        if (data.status) {
                            regionsAdapter.dataSource = data.data
                            regionsAdapter.notifyDataSetChanged()

                            if (arguments != null) {
                                val dataArg =
                                    requireArguments().getParcelable<com.nurbk.ps.homebusness.model.profile.Data>(
                                        DATA_PROFILE
                                    )!!
                                for (city in data.data) {
                                    val dataCountry = city
                                    for (cites in dataArg.regions!!) {
                                        if (dataCountry.id == cites.regionId) {
                                            val region = Region(
                                                dataCountry.id,
                                                dataCountry.title,
                                                cites.price.toString()
                                            )
                                            regionAdapter.data.add(region)
                                            regionAdapter.notifyDataSetChanged()
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        viewModel.getDeliveryTimes()
        viewModelCategory.dataCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        if (data.status) {
                            categoryAdapter.dataSource = data.data
                            categoryAdapter.notifyDataSetChanged()
                            viewModel.dataSubCategoryLiveData.observe(viewLifecycleOwner, Observer {
                                when (it) {
                                    is Resource.Success -> {
                                        Timber.d(" onViewCreated->Resource.Success")
                                        it.data?.let { data ->
                                            Constant.dialog.hide()

                                            if (data.status) {
                                                var names = ArrayList<Data>()
                                                names.add(0, Data(0, ArrayList(), getString(R.string.select_cate), ""))
                                                names.addAll(data.data as ArrayList<Data>)
                                                categorySub1Adapter.dataSource = names
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
                    Constant.showDialog(requireActivity())
                }
            }
        })

        mBinding.spineerEditMain.adapter = categoryAdapter



        viewModel.dataDeliveryLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Constant.dialog.hide()

                        if (data.status) {
                            deliveryAdapter.dataSource = data.data.data
                            deliveryAdapter.notifyDataSetChanged()

                            if (arguments != null) {
                                val dataArg =
                                    requireArguments().getParcelable<com.nurbk.ps.homebusness.model.profile.Data>(
                                        DATA_PROFILE
                                    )!!
                                for (i in data.data.data.indices) {
                                    if (dataArg.deliveryTime == data.data.data[i].id) {
                                        mBinding.spinnerTimeDelivery.setSelection(i)
                                        break
                                    }
                                }
                            }

                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })
        mBinding.spinnerTimeDelivery.adapter = deliveryAdapter

        mBinding.spineerEditMain.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val data =
                        categoryAdapter.dataSource[position] as com.nurbk.ps.homebusness.model.DataCategories.Data
                    if (data.subCategory != 0) {
                        mBinding.group2.visibility = View.VISIBLE
                        viewModel.getSubCategories(data.id.toString())
                    } else {
                        mBinding.group2.visibility = View.GONE

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        mBinding.spnnerDelivery.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mBinding.isEnableService = position != 0
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        mBinding.spinnerEditOne.adapter = categorySub1Adapter
        mBinding.spinnerEditTow.adapter = categorySub1Adapter

        mBinding.spinnerGover.adapter = spinnerAdapter
        mBinding.spinnerRegion.adapter = regionsAdapter
        btn_back.setOnClickListener {
            findNavController().navigateUp()
        }

        val timePickerDialogStart = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minutes ->
                mBinding.txtStartStore.setText("${hourOfDay}:${minutes}")
            }, 0, 0, false
        )

        val timePickerDialogClose = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minutes ->
                mBinding.txtFinshStory.setText("${hourOfDay}:${minutes}")
            }, 0, 0, false
        )

        viewModel.dataMarketLiveData.observe(
            viewLifecycleOwner,
            Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Constant.dialog.hide()
                            Log.e("ttttttttt", "${data.code}")

                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.request),
                                    Snackbar.LENGTH_LONG
                                ).show()
//                                findNavController().navigateUp()

                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Constant.dialog.hide()

                        }
                    }
                    is Resource.Loading -> {
                        Constant.showDialog(requireActivity())
                    }
                }
            })

        mBinding.btnSaveStore.setOnClickListener {
            editStore()
        }

        mBinding.btnSaveGover.setOnClickListener {
            val nameGover = mBinding.spinnerGover.selectedItem as Data
            val price = mBinding.txtAddPriceGover.text.toString()
            if (mBinding.linearLayout5.isVisible && price.isEmpty()) {
                mBinding.txtAddPriceGover.error = getString(R.string.errorMessage)
                mBinding.txtAddPriceGover.requestFocus()
                return@setOnClickListener
            }
            nameGover.price = price
            mBinding.txtAddPriceGover.text.clear()
            cityAdapter.data.add(nameGover)
            cityAdapter.notifyDataSetChanged()
        }

        mBinding.btnSaveRegion.setOnClickListener {
            val nameRegion = mBinding.spinnerRegion.selectedItem as Data
            val priceRegion = mBinding.txtAddPriceRegion.text.toString()
            if (mBinding.linearLayout5.isVisible && priceRegion.isEmpty()) {
                mBinding.txtAddPriceRegion.error = getString(R.string.errorMessage)
                mBinding.txtAddPriceRegion.requestFocus()
                return@setOnClickListener
            }
            regionAdapter.data.add(Region(nameRegion.id, nameRegion.title, priceRegion))
            regionAdapter.notifyDataSetChanged()
        }

        mBinding.rcDataGover.apply {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mBinding.rcDataRegion.apply {
            adapter = regionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mBinding.txtStartStore.setOnClickListener {
            timePickerDialogStart.show()
        }
        mBinding.txtFinshStory.setOnClickListener {
            timePickerDialogClose.show()
        }

        mBinding.txtLocationStore.setOnClickListener {
            MapDialog(this).show(childFragmentManager, "")

        }
        mBinding.btnAddImage.setOnClickListener {
            permissionImger(requireContext()) {
                selectImage()
            }
        }


        if (arguments != null) {
            val data =
                requireArguments().getParcelable<com.nurbk.ps.homebusness.model.profile.Data>(
                    DATA_PROFILE
                )!!


            Constant.setImage(
                requireContext(),
                data.avatar,
                mBinding.btnAddImage,
                R.drawable.store_placeholder
            )

            mBinding.txtEditName.setText(data.name)
            mBinding.txtEditDescription.setText(data.note)
            //TODO : Email Not in API
            mBinding.txtEditEmail.setText("")
            mBinding.txtEditDescription.setText(data.note)

            mBinding.spnnerDelivery.setSelection(data.delivery?:-1)
            if (data.delivery == 0){
                mBinding.isEnableService = false
            }else if (data.delivery == 1){
                mBinding.isEnableService = true
                getItemPosition(categoryAdapter.dataSource
                        , data.catId?:-1
                        , mBinding.spineerEditMain)

                getItemPosition(
                        categorySub1Adapter.dataSource,
                        data.subCatId?:-1,
                        mBinding.spinnerEditOne
                )
                getItemPosition(
                        categorySub1Adapter.dataSource,
                        data.subCatId2?:-1,
                        mBinding.spinnerEditTow
                )
            }

            // TODO : not in Api
            mBinding.spinnerReceive.setSelection(0)

            mBinding.txtStartStore.setText(data.openFrom)
            mBinding.txtFinshStory.setText(data.openTo)

            // TODO : not in Api
            mBinding.chSendWhatsApp.isChecked = false
            // TODO : not in Api
            mBinding.chReceiveColl.isChecked = false

            mBinding.txtMniPrice.setText(data.minOrder.toString())

            map["lat"] = toRequestBody(data.lat.toString())
            map["lng"] = toRequestBody(data.lng.toString())

            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            val addresses = geocoder.getFromLocation(
                data.lat?.toDouble()?:0.0,
                data.lng?.toDouble()?:0.0,
                1
            )
            try {
                val city: String = addresses[0].locality
                mBinding.txtLocationStore.setText(city)
            } catch (e: Exception) {
                mBinding.txtLocationStore.setText(getString(R.string.done))
            }

        }
    }

    private fun getItemPosition(data: List<*>, id: Int, spinner: Spinner) {
        for (i in data.indices) {
            val categoryMain =
                (categoryAdapter.dataSource[i] as com.nurbk.ps.homebusness.model.DataCategories.Data)
            if (categoryMain.id == id) {
                spinner.setSelection(i)
                break
            }

        }
    }

    private var partImage = MultipartBody.Part.createFormData("", "", toRequestBody(""))

    private fun editStore() {
        val name = mBinding.txtEditName.text.toString()
        val description = mBinding.txtEditDescription.text.toString()
        val email = mBinding.txtEditEmail.text.toString()

        val main =
            mBinding.spineerEditMain.selectedItem as com.nurbk.ps.homebusness.model.DataCategories.Data


        val delivery = mBinding.spnnerDelivery.selectedItemPosition
        val receive = mBinding.spinnerReceive.selectedItemPosition

        val open = mBinding.txtStartStore.text.toString()
        val close = mBinding.txtFinshStory.text.toString()

        val price = mBinding.txtMniPrice.text.toString()

        val location = mBinding.txtLocationStore.text.toString()

        val whatsApp = mBinding.chSendWhatsApp.isChecked
        val coll = mBinding.chReceiveColl.isChecked
        val deliveryTime =
            mBinding.spinnerTimeDelivery.selectedItem as DataX

        if (mBinding.group2.isVisible) {
            val mainOne =
                mBinding.spinnerEditOne.selectedItem as com.nurbk.ps.homebusness.model.DataCategories.Data
            val mainTow =
                mBinding.spinnerEditTow.selectedItem as com.nurbk.ps.homebusness.model.DataCategories.Data

            if (mBinding.spinnerEditOne.selectedItemPosition == 0){
                Snackbar.make(requireView(), getString(R.string.no_select), Snackbar.LENGTH_SHORT).show()
            }else{
                map["sub_cat_id"] = toRequestBody(mainOne.id.toString())
            }
            if (mBinding.spinnerEditTow.selectedItemPosition != 0){
                map["sub_cat_id_2"] = toRequestBody(mainTow.id.toString())
            }
        }

        if (name.isEmpty()) {
            mBinding.txtEditName.error = getString(R.string.errorMessage)
            mBinding.txtEditName.requestFocus()
            return
        }

        if (description.isEmpty()) {
            mBinding.txtEditDescription.error = getString(R.string.errorMessage)
            mBinding.txtEditDescription.requestFocus()
            return
        }
        if (email.isEmpty()) {
            mBinding.txtEditEmail.error = getString(R.string.errorMessage)
            mBinding.txtEditEmail.requestFocus()
            return
        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            mBinding.txtEditEmail.error = "Please Enter a Valid Email"
//            mBinding.txtEditEmail.requestFocus()
//            return
//        }

        if (cityAdapter.data.size == 0) {
            Snackbar.make(
                requireView(),
                getString(R.string.errorGovernorate),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (regionAdapter.data.size == 0) {
            Snackbar.make(
                requireView(),
                getString(R.string.errorRegion),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (open.isEmpty()) {
            mBinding.txtStartStore.error = getString(R.string.errorMessage)
            mBinding.txtStartStore.requestFocus()
            return
        }

        if (close.isEmpty()) {
            mBinding.txtFinshStory.error = getString(R.string.errorMessage)
            mBinding.txtFinshStory.requestFocus()
            return
        }

        if (close == open) {
            mBinding.txtStartStore.requestFocus()
            mBinding.txtFinshStory.requestFocus()
            Snackbar.make(
                requireView(),
                getString(R.string.errorTime),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (price.isEmpty() || price == "0") {
            mBinding.txtMniPrice.error = getString(R.string.errorMessage)
            mBinding.txtMniPrice.requestFocus()
            return
        }

        if (location.isEmpty()) {
            mBinding.txtLocationStore.error = getString(R.string.errorMessage)
            mBinding.txtLocationStore.requestFocus()
            return
        }

        if (file == null) {
            Snackbar.make(
                requireView(),
                getString(R.string.addPhotoStore),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        map["name"] = toRequestBody(name)
        map["note"] =
            toRequestBody(description)
        map["email"] = toRequestBody(email)
        map["cat_id"] = toRequestBody(main.id.toString())


        map["delivery"] = toRequestBody(delivery.toString())
        map["market_status"] = toRequestBody(receive.toString())
        map["open_from"] = toRequestBody(open)
        map["open_to"] = toRequestBody(close)
        map["allow_whats"] = toRequestBody(if (whatsApp) "1" else "0")
        map["allow_call"] = toRequestBody(if (coll) "1" else "0")
        map["delivery_time"] = toRequestBody(deliveryTime.id.toString())
        map["min_order"] = toRequestBody(price)

        if (mBinding.spnnerDelivery.selectedItemPosition == 1){
            for ((i, cities) in cityAdapter.data.withIndex()) {

                map["cities[$i][city_id]"] = toRequestBody((cities as Data).id.toString())
                map["cities[$i][price]"] = toRequestBody(cities.price)
            }

            for ((i, cities) in regionAdapter.data.withIndex()) {
                map["regions[$i][region_id]"] = toRequestBody((cities as Region).id.toString())
                map["regions[$i][price]"] = toRequestBody(cities.price)
            }

        }
        if (file != null) {
            val imagefile = File(getRealPathFromURI(requireContext(), Uri.parse(file)!!)!!)
            val reqBody = RequestBody.create("image".toMediaTypeOrNull(), imagefile)
            partImage = MultipartBody.Part.createFormData("image", imagefile.name, reqBody)
        }

        viewModel.getDataMarkets(
            map,
            partImage
        )
    }


    override fun onClick(item: Any, type: Int) {
        if (type == 1) {
            cityAdapter.data.remove(item)
            cityAdapter.notifyDataSetChanged()
        } else {
            regionAdapter.data.remove(item)
            regionAdapter.notifyDataSetChanged()
        }
    }

    override fun getLocationMap(latLng: LatLng) {

        map["lat"] = toRequestBody(latLng.latitude.toString())
        map["lng"] = toRequestBody(latLng.longitude.toString())

        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        val addresses = geocoder.getFromLocation(
            latLng.latitude,
            latLng.longitude,
            1
        )
        try {
            val city: String = addresses[0].locality
            mBinding.txtLocationStore.setText(city)
        } catch (e: NullPointerException) {
            mBinding.txtLocationStore.setText(getString(R.string.done))
        }
    }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/png"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    var file: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {
            file = data!!.data.toString()
            mBinding.btnAddImage.setImageURI(data.data)
        }
    }


}