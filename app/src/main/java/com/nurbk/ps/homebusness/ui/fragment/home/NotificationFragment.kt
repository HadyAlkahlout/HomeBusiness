package com.nurbk.ps.homebusness.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.adapter.NotificationAdapter
import com.nurbk.ps.homebusness.databinding.FragmentNotificationBinding
import com.nurbk.ps.homebusness.model.DataNotification.Notification
import com.nurbk.ps.homebusness.ui.viewmodel.home.NotificationViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.OnScrollListener
import com.nurbk.ps.homebusness.util.Resource
import timber.log.Timber

class NotificationFragment : Fragment(), NotificationAdapter.OnClickListener {

    private lateinit var mBinding: FragmentNotificationBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val notificationAdapter =
        NotificationAdapter(ArrayList(), this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNotificationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        Constant.showDialog(requireActivity())

        viewModel.getNotification()
        viewModel.dataNotificationLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Timber.d(" onViewCreated->Resource.Success")
                    it.data?.let { data ->

                        hideProgressBar()
                        if (data.status) {
                            onScrollListener.totalCount = data.data.countTotal
                            notificationAdapter.apply {
                                this.data.apply {
                                    clear()
                                    addAll(data.data.data)
                                }
                                notifyDataSetChanged()
                            }

                        }
                    }
                }
                is Resource.Error -> {
                    Constant.showDialog(requireActivity())
                    hideProgressBar()

                    it.message?.let { message ->
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        })
        loadNotification()
    }

    private fun hideProgressBar() {
        Constant.dialog.hide()
        Timber.d("hideProgressBar")
        isLoading = false
    }

    private fun showProgressBar() {
        Timber.d(" showProgressBar")
        isLoading = true
    }

    private val onScrollListener = OnScrollListener(isLoading, isLastPage, 0) {
        viewModel.getNotification()
        isScrolling = false
    }


    private fun loadNotification() {
        mBinding.listNotifiaction.apply {
            adapter = notificationAdapter
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onClickItem(data: Notification, position: Int) {
        viewModel.readNotification(data.id.toString())
        data.read = 1
        notificationAdapter.notifyItemChanged(position)
    }

}