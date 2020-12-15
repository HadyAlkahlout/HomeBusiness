package com.nurbk.ps.homebusness.ui.fragment.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.PaymentAdapter
import com.nurbk.ps.homebusness.databinding.FragmentCompleteOrderBinding
import com.nurbk.ps.homebusness.model.Country.Region
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.model.cart.City
import com.nurbk.ps.homebusness.model.cart.DataX
import com.nurbk.ps.homebusness.model.promocode.Data
import com.nurbk.ps.homebusness.ui.fragment.dialog.AddressDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.cart.CheckoutViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.toRequestBody
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_complete_order.*
import okhttp3.RequestBody
import timber.log.Timber


class CompleteOrderFragment : Fragment(), PaymentAdapter.onClick,
    AddressDialogFragment.OnClickAddress {

    private lateinit var mBinding: FragmentCompleteOrderBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[CheckoutViewModel::class.java]
    }


    private val payment_adapter by lazy {
        PaymentAdapter(requireActivity(), ArrayList(), this)
    }


    private val arg_Array by lazy {
        requireArguments().getParcelableArrayList<DataX>("array")
    }

    private val arg_city by lazy {
        requireArguments().getParcelableArrayList<City>("array_city")
    }

    private val arg_region by lazy {
        requireArguments().getParcelableArrayList<com.nurbk.ps.homebusness.model.cart.Region>("array_region")
    }

    private val arg_note by lazy {
        requireArguments().getString("note")
    }

    private val arg_total by lazy {
        requireArguments().getInt("total")
    }

    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }


    var discount_code = ""
    lateinit var address : Content
    var payment_id: Long = 1
    var cityid = ""
    private val map: MutableMap<String, RequestBody> = HashMap()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCompleteOrderBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        txt_total.text = arg_total.toString()
        txt_total_with_promo.text = arg_total.toString()


        textView15.setOnClickListener {
            AddressDialogFragment(this, 1,arg_city!!,arg_region!!).show(requireActivity().supportFragmentManager, "")
        }


        viewModel.dataPaymentLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        if (data.status) {

                            if (data.data == null) {
                            } else {
                                payment_adapter.data.clear()
                                payment_adapter.data.addAll(data.data)
                                payment_adapter.notifyDataSetChanged()
                            }
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })



        viewModel.dataPromoCodeLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        if (data.status){
                            discount_code = data.data.discount
                            txt_discount_cost.text=data.data.discount
                            txt_total_with_promo.text = ((arg_total.toString().toInt() * data.data.discount.toInt()).toString())
                        }else{
                            discount.visibility=View.GONE
                            txt_discount_cost.visibility=View.GONE
                        }
                        Constant.dialog.dismiss()
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                    if (response.message.equals("Internal Server Error")) {
                        Snackbar.make(mBinding.root,requireActivity().getString(R.string.codeUnavailable), Snackbar.LENGTH_SHORT)
                            .show()
                    }

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                }
            }
        })


        btn_add_promo_code.setOnClickListener {
            if (etxt_promo_code.text.toString().isNotEmpty()) {
                viewModel.PostPromoCode(Data(etxt_promo_code.text.toString()))
            }
        }


        btnCompeletOrder.setOnClickListener {
            if (cityid == "") {
                mBinding.textView15.error = getString(R.string.empty_felid)
                mBinding.textView15.requestFocus()
                return@setOnClickListener
            }


            map["payment_id"] = toRequestBody(payment_id.toString())
            Log.e("city id",cityid)
            map["address_id"] = toRequestBody(cityid)
            if (etxt_promo_code.text.toString().isNotEmpty())
                map["promo_code"] = toRequestBody(etxt_promo_code.text.toString())

            map["notes"] = toRequestBody(arg_note ?: "")
           for (i in 0 until arg_Array!!.size){
               Log.e("array i ",i.toString())
                map["products[$i][item_id]"] = toRequestBody(arg_Array!![i].itemId.toString())
                map["products[$i][number]"] = toRequestBody(arg_Array!![i].number.toString())
                map["products[$i][note]"] = toRequestBody(arg_Array!![i].note!!)
            }

            Log.e("eee checkout",map.toString())
            viewModel.PostCheckout(map)

        }


        viewModel.dataCheckoutLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()
                        if (data.status) {
                            getShare.edit().putString(Constant.MARKET_ID, "").apply()
                            Log.e("ttttcode status", "${data.code}")
                            val bundle = Bundle().apply {
                                putParcelableArrayList("array", arg_Array)
                                putInt("total",arg_total)
                                putString("discount_code",discount_code)
                                putParcelable("address",address)
                            }
                            Snackbar.make(
                                    mBinding.root,
                                    getString(R.string.paymentCart),
                                    Snackbar.LENGTH_SHORT
                            )
                                    .show().also {
                                        findNavController().navigate(R.id.action_completeOrderFragment_to_checkoutFragment,bundle)

                                    }
                        }else{
                            Log.e("ttttcode", "${data.code}")
                        }


                    }
                }
                is Resource.Error -> {
                    Log.e("ttttcode error", response.message.toString())
                }
                is Resource.Loading -> {
                }
            }
        })

        list_payment.layoutManager = LinearLayoutManager(requireContext())
        list_payment.adapter = payment_adapter
    }

    override fun onClick(
        data: com.nurbk.ps.homebusness.model.payment.Content,
        position: Int,
        type: Int
    ) {
        when (type) {
            1 -> {
                payment_id = data.id
                payment_adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClickAddress(data: Content) {
        mBinding.textView15.text = data.title
        cityid=data.id.toString()
        address = data
       // address_id = country.id*/
        Log.e("eeee address","$data.city_id  | $data.region_id")
    }


}