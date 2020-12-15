package com.nurbk.ps.homebusness.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.adapter.QuestionAdapter
import com.nurbk.ps.homebusness.databinding.FragmentQuestionBinding
import com.nurbk.ps.homebusness.ui.viewmodel.profile.QuestionViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_question.*
import timber.log.Timber

class QuestionFragment : Fragment() {

    private lateinit var mBinding: FragmentQuestionBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[QuestionViewModel::class.java]
    }

    val title_array = ArrayList<String>()
    val body: MutableList<MutableList<String>> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentQuestionBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        var season: MutableList<String> = ArrayList()
        viewModel.dataQuestionLiveData.observe(viewLifecycleOwner, Observer { response ->
            Timber.e(" onViewCreated->viewModel")
            when (response) {
                is Resource.Success -> {
                    Timber.e(" onViewCreated->Resource.Success")
                    response.data?.let { data ->
                        dialog.dismiss()
                        if (data.status) {
                            for (i in data.data.data.indices) {
                                season = (season + i.toString()).toMutableList()
                                title_array.add(data.data.data[i].question)
                                season.clear()
                                season.add(data.data.data[i].answer)
                                body.add(season)
                            }

                            expandableListView.setAdapter(
                                QuestionAdapter(
                                    requireContext(),
                                    expandableListView,
                                    title_array,
                                    body
                                )
                            )
                            Log.e("eee data", data.data.data.toString())
                        } else {
                            Timber.e("eee success ${data.code}")
                        }
                    }
                }
                is Resource.Error -> {
                    dialog.dismiss()
                    Log.e("eee error", response.message.toString())

                }
                is Resource.Loading -> {
                    showDialog(requireActivity())
                    Log.e("eee error", response.message.toString())
                }
            }
        })

        mBinding.button2.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.CALL, "callus")
            findNavController().navigate(
                R.id.action_questionFragment_to_callFragment,
                bundle
            )
        }

    }

}