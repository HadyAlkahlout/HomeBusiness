package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.FavoriteAdapter
import com.nurbk.ps.homebusness.databinding.FragmentFavoriteProfileBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.favorite.Content
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.ui.viewmodel.profile.FavoriteViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_favorite.*
import timber.log.Timber

class FavoriteProfileFragment:Fragment() ,
    FavoriteAdapter.OnClickItem {

    private val adapterFav = FavoriteAdapter(ArrayList(), this)
    private lateinit var mBinding: FragmentFavoriteProfileBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    private val viewModelDeleteFav by lazy {
        ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavoriteProfileBinding.inflate(inflater,container,false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding. toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.dataFavoriteLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        dialog.dismiss()

                        if (data.status) {
                            adapterFav.data.clear()
                            adapterFav.data.addAll(data.data.data)
                            adapterFav.notifyDataSetChanged()
                            if (data.data.data.size != 0) {
                               mBinding.itemFav.visibility = View.GONE
                            }else{
                                mBinding.itemFav.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                   showDialog(requireActivity())
                }
            }
        })

        mBinding.rcDataFav.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = adapterFav

        }


    }

    override fun onClickListener(content: Content,position:Int, type: Int) {
        when(type) {
            1 -> {
                val bundle = Bundle()
                bundle.putString(Constant.DETAILS_PRODUCT, content.id.toString())
        findNavController().navigate(
            R.id.action_favoriteProfileFragment_to_productDetailsFragment,
            bundle
        )
            }
            2 -> {

            }
            3 -> {
                viewModelDeleteFav.deleteFav(ClassID(content.id.toString()))
                adapterFav.data.remove(content)
                adapterFav.notifyItemRemoved(position)

                if (adapterFav.data.size != 0) {
                    item_fav.visibility = View.GONE
                } else {
                    item_fav.visibility = View.VISIBLE
                }
            }
         }
    }

}