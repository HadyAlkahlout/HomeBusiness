package com.nurbk.ps.homebusness.ui.fragment.cart

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.CartAdapter
import com.nurbk.ps.homebusness.databinding.FragmentCartBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.cart.City
import com.nurbk.ps.homebusness.model.cart.DataX
import com.nurbk.ps.homebusness.model.cart.Region
import com.nurbk.ps.homebusness.ui.viewmodel.cart.CartViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.MARKET_ID
import com.nurbk.ps.homebusness.util.Resource
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_cart.*
import timber.log.Timber

class CartFragment : Fragment(), CartAdapter.OnClickItem {


    private lateinit var mBinding: FragmentCartBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    var counter = 1

    private val cart_adapter by lazy {
        CartAdapter(ArrayList(), this)
    }

    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }


    var total = 0

    lateinit var city_array:ArrayList<City>
    lateinit var region_array:ArrayList<Region>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCartBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        cart_list.apply {
            adapter = cart_adapter
            val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
                    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                    viewModel.deleteOrders(
                        ClassID(
                            cart_adapter.data[viewHolder.adapterPosition].id!!.toLong().toString()
                        )
                    )
                            .also {
                                cart_adapter.notifyItemRemoved(swipeDir)
                            }

                }
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addBackgroundColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                android.R.color.holo_red_dark
                            )
                        )
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate()
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }


        item_cart.visibility = View.GONE
        contaner.visibility = View.GONE

        viewModel.dataCartLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->

                        if (data.status) {
                            if (data.data == null) {
                                item_cart.visibility = View.VISIBLE
                                contaner.visibility = View.GONE
                                Constant.dialog.dismiss()
                                getShare.edit().remove(MARKET_ID).apply()
                            } else {
                                item_cart.visibility = View.GONE
                                contaner.visibility = View.VISIBLE
                                cart_adapter.data.clear()
                                cart_adapter.data.addAll(data.data.data!!)
                                cart_adapter.data.forEach {
                                    total += ((it.price.toDouble() * it.number)).toInt()
                                }

                                region_array =
                                    (data.data.addresses!!.regions as ArrayList<Region>?)!!
                                city_array = (data.data.addresses.cities as ArrayList<City>?)!!

                                cart_adapter.notifyDataSetChanged()
                                total += cart_adapter.total
                                txt_sub_price.text = total.toString()
                                txt_Total_cost.text = total.toString()
                                Constant.dialog.dismiss()
                                Log.e("eee cart ", data.data!!.data.toString())
                                Log.e("eee cart address", data.data!!.addresses.toString())
                            }
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                }
            }
        })




        viewModel.dataDeleteLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()

                        if (data.status) {
                            if (cart_adapter.data.size == 0) {
                                getShare.edit().putString(Constant.MARKET_ID, "").apply()
                                item_cart.visibility = View.VISIBLE
                                contaner.visibility = View.GONE
                                Log.e("eee", "eee null")
                                //    Constant.dialog.dismiss()
                            } else {
                                cart_adapter.data.clear()
                                viewModel.dataCartLiveData.value = null
                                viewModel.getMyCart()
                                item_cart.visibility = View.GONE
                                contaner.visibility = View.VISIBLE
                                cart_adapter.data = cart_adapter.data

                                cart_adapter.data.forEach {
                                    total += ((it.price.toDouble() * it.number)).toInt()
                                }
                                txt_sub_price.text = total.toString()
                                txt_Total_cost.text = total.toString()

                                cart_adapter.notifyDataSetChanged()
                                //    Constant.dialog.dismiss()
                            }
                        }

                    }
                }
                is Resource.Error -> {
                    // Constant.dialog.dismiss()
                }
                is Resource.Loading -> {
                    //  Constant.showDialog(requireActivity())
                }
            }
        })





        completeBtnOrder.setOnClickListener {
            var arr = arrayOfNulls<DataX>(cart_adapter.data.size)
            arr = cart_adapter.data.toArray(arr)
            val bundle = Bundle().apply {

                putParcelableArrayList("array", cart_adapter.data)
                putParcelableArrayList("array_region", region_array)
                putParcelableArrayList("array_city", city_array)
                putString("note", editText3.text.toString())
                putInt("total", total)
            }
            findNavController().navigate(R.id.action_cartFragment_to_completeOrderFragment, bundle)
        }

        btn_add_product.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }

    }

    override fun onClickListener(order: DataX, type: Int, position: Int) {

        when (type) {
            0 -> {
                cart_adapter.notifyItemChanged(position)


                total += order.price
                order.number += 1
                txt_sub_price.text = total.toString()
                txt_Total_cost.text = total.toString()
            }
            1 -> {
                if (order.number > 1) {
                    order.number -= 1
                    cart_adapter.notifyItemChanged(position)

                    total -= order.price

                    txt_sub_price.text = total.toString()
                    txt_Total_cost.text = total.toString()
                }
            }
        }
    }

}