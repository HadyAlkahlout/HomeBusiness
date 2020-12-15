package com.nurbk.ps.homebusness.ui.fragment.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.CheckoutAdapter
import com.nurbk.ps.homebusness.databinding.FragmentCheckoutBinding
import com.nurbk.ps.homebusness.model.Order
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.model.cart.DataX
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.USERNAME
import kotlinx.android.synthetic.main.fragment_checkout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutFragment : Fragment() {

    private lateinit var mBinding: FragmentCheckoutBinding

    private val adapter by lazy {
        CheckoutAdapter(ArrayList())
    }

    private val arg_Array by lazy {
        requireArguments().getParcelableArrayList<DataX>("array")
    }


    private val arg_address by lazy {
        requireArguments().getParcelable<Content>("address")
    }

    private val arg_total by lazy {
        requireArguments().getInt("total")
    }

    private val arg_discount by lazy {
        requireArguments().getString("discount_code")
    }

    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCheckoutBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }


        txt_userName.text = getShare.getString(USERNAME,"")
        Log.e("eee username",getShare.getString(USERNAME,"").toString())
        txt_total.text = arg_total.toString()
        txt_userAddress.text =arg_address!!.title

        if (arg_discount == ""){
            txt_discount_cost.visibility =View.GONE
            discount.visibility =View.GONE
        }
        txt_discount_cost.text = arg_discount.toString()
        txt_order_status.text = requireActivity().getString(R.string.done)

        txt_order_date.text = SimpleDateFormat("YYYY-MM-DD",Locale.getDefault()).format(Calendar.DATE)

     adapter.data.clear()
     adapter.data.addAll(arg_Array!!)
        adapter.notifyDataSetChanged()



        list_order.adapter = adapter

        mBinding.btnBackToHome.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }

    }

}