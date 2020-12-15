package com.nurbk.ps.homebusness.ui.fragment.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.DialogItemAdapter
import com.nurbk.ps.homebusness.model.packages.Content
import com.nurbk.ps.homebusness.model.packages.PostPackageID
import com.nurbk.ps.homebusness.model.packages.PostProductPackage
import com.nurbk.ps.homebusness.ui.viewmodel.store.DialogViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.store.PostUpgradeViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.item_subscripition.view.*
import kotlinx.android.synthetic.main.item_subscripition.view.list_item_dialog
import kotlinx.android.synthetic.main.item_tameez_product.view.*
import timber.log.Timber

class TamezMarketFragment(val onGo: GoFragmentMessage,val type: Int) :
    BottomSheetDialogFragment(),DialogItemAdapter.OnClickListener {


    private val viewModel by lazy {
        ViewModelProvider(this)[DialogViewModel::class.java]
    }

    private val viewModel2 by lazy {
        ViewModelProvider(this)[PostUpgradeViewModel::class.java]
    }

    private val adapter by lazy {
        DialogItemAdapter(ArrayList(),this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.setCancelable(false)
        if (type == 0) {
            return inflater.inflate(R.layout.item_tameez_store, container, false)
        }else if (type == 1){
            return inflater.inflate(R.layout.item_tameez_product, container, false)
        }else{
            return inflater.inflate(R.layout.item_subscripition, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)


        viewModel.dataMyDialogLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()
                        adapter.data.clear()
                        adapter.data.addAll(data.data.data)
                        adapter.notifyDataSetChanged()
                        Log.e("eee packages", data.data.data.toString())
                        if (data.status) {
                            Timber.e("eee success ${data.data.data}")
                        }
                    }
                }
                is Resource.Error -> {
                    Constant.dialog.dismiss()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })

        viewModel2.dataPostDialogLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        Constant.dialog.dismiss()
                        dismiss()
                        onGo.onClick(true)
                        Log.e("eee packages", data.toString())
                        if (data.status) {
                            Timber.e("eee success $data")
                        }
                    }
                }
                is Resource.Error -> {
                    onGo.onClick(false)
                    Constant.dialog.dismiss()
                    dismiss()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    Constant.showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (type == 2) {
            viewModel.getSubscribe()
        }else if (type == 1){
            viewModel.getTameezProduct()
        }else{
            viewModel.getTameezStore()
            Log.e("Eee","eee 0")
        }
    }




    private fun setupClickListeners(view: View) {

        if (type == 2) {
            view.list_item_dialog.adapter = adapter
        }else if (type == 1){
            view.list_item_dialog.adapter = adapter
            Log.e("eee","eee 11")
        }else{
            view.list_item_dialog.adapter = adapter
            Log.e("eee","eee 0")
        }

    }



    interface GoFragmentMessage {
        fun onClick(type: Boolean)
    }

    @SuppressLint("LongLogTag")
    override fun onClickItem(data: Content, id: Long) {
        Log.e("Eeee click",id.toString())
        if (type == 2) {
            viewModel2.upgradeAccount(PostPackageID(id))
        }else if (type == 1){
            viewModel2.upgradeToSpecialProduct(PostProductPackage(id,requireActivity().intent.getLongExtra("id",1)))
        }else{
            Log.e("Eeee upgradeToSpecialAccount ",id.toString())
            viewModel2.upgradeToSpecialAccount(PostPackageID(id))
        }
        //dialog
    }

}