package com.nurbk.ps.homebusness.ui.fragment.category

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
import com.nurbk.ps.homebusness.adapter.CategoryAdapter
import com.nurbk.ps.homebusness.databinding.FragmentCategoriesBinding
import com.nurbk.ps.homebusness.model.DataCategories.Data
import com.nurbk.ps.homebusness.ui.viewmodel.category.CategoriesViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.TYPE_STORE
import com.nurbk.ps.homebusness.util.Resource
import timber.log.Timber

class CategoriesFragment : Fragment(), CategoryAdapter.OnClickItem {


    private lateinit var mBinder: FragmentCategoriesBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[CategoriesViewModel::class.java]
    }

    private val categoriesAdapter = CategoryAdapter(ArrayList(), this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinder = FragmentCategoriesBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.dataCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->
                        Constant.dialog.hide()
                        if (data.status) {
                            categoriesAdapter.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data)
                                }
                                notifyDataSetChanged()
                            }

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
        loadDataCategories()
    }

    private fun loadDataCategories() {
        mBinder.rcDataCategories.apply {
            adapter = categoriesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    override fun onClick(dataCategoires: Data) {
        val bundle = Bundle()
        bundle.putString(TYPE_STORE, dataCategoires.id.toString())
        findNavController().navigate(
            R.id.action_categoriesFragment_to_storeCategoryFragment,
            bundle
        )
    }
}