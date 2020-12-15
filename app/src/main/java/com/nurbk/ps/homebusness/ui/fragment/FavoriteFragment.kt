package com.nurbk.ps.homebusness.ui.fragment

import android.os.Bundle
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
import com.nurbk.ps.homebusness.adapter.FavoriteAdapter
import com.nurbk.ps.homebusness.databinding.FragmentFavoriteBinding
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

class FavoriteFragment : Fragment(),
    FavoriteAdapter.OnClickItem {

    private val adapterFav = FavoriteAdapter(ArrayList(), this)
    private lateinit var mBinding: FragmentFavoriteBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }
    private val viewModelDeleteFav by lazy {
        ViewModelProvider(this)[ProductDetailsViewModel::class.java]
    }

    private val getShare by lazy {
        Constant.getSharePref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        viewModel.dataFavoriteLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        dialog.hide()
                        if (data.status) {
                            adapterFav.data.clear()
                            adapterFav.data.addAll(data.data.data)
                            adapterFav.notifyDataSetChanged()
                            if (data.data.data.size != 0) {
                                item_fav.visibility = View.GONE
                            } else {
                                item_fav.visibility = View.VISIBLE
                            }
                            Timber.e("eee success ${data.data.data}")
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.hide()

                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                }
            }
        })

        rcDataFav.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterFav

        }

        viewModelDeleteFav.deleteLiveData.observe(viewLifecycleOwner,
            Observer { response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            if (data.status) {
                                Snackbar.make(
                                    requireView(),
                                    "تم الازالة من المضلة",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                                viewModelDeleteFav.deleteLiveData.value = null
                            } else if (data.code == 121) {
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.alreadyAdded),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })
    }

    override fun onClickListener(content: Content, position: Int, type: Int) {
        if (getShare.getString(Constant.TOKEN, "") == null || getShare.getString(
                Constant.TOKEN, ""
            ).toString()
                .isEmpty()
        ) {
            Snackbar.make(
                mBinding.root,
                getString(R.string.singin),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        when (type) {
            1 -> {

                val bundle = Bundle()
                bundle.putString(Constant.DETAILS_PRODUCT, content.id.toString())
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_productDetailsFragment,
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