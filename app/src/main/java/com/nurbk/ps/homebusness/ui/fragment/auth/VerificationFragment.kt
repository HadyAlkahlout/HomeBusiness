package com.nurbk.ps.homebusness.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nurbk.ps.homebusness.databinding.FragmentCategoriesBinding
import com.nurbk.ps.homebusness.databinding.FragmentVerificationBinding
import com.nurbk.ps.homebusness.model.user.ActivateAccount
import com.nurbk.ps.homebusness.ui.activity.MainActivity
import com.nurbk.ps.homebusness.ui.viewmodel.auth.AuthViewModel
import com.nurbk.ps.homebusness.util.Constant.LOGIN
import com.nurbk.ps.homebusness.util.Constant.dialog
import com.nurbk.ps.homebusness.util.Constant.editor
import com.nurbk.ps.homebusness.util.Constant.showDialog
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.fragment_verification.*
import timber.log.Timber
import java.util.*

class VerificationFragment : Fragment() {

    private lateinit var mBinding: FragmentVerificationBinding
    private var mCountDownTimer: CountDownTimer? = null

    private var mStartTimeInMillis: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0
    private var mTimerRunning = true

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private val edit
            by lazy { editor(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mBinding = FragmentVerificationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnVerify.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        viewModel.dataStatusLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            dialog.hide()
                            if (data.status) {
                                edit.putBoolean(LOGIN, true)
                                edit.apply()
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        MainActivity::class.java
                                    )
                                ).also {
                                    requireActivity().finish()
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message!!}")
                        dialog.hide()

                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                       showDialog(requireActivity())

                    }
                }
            })

        viewModel.dataResendLiveData.observe(viewLifecycleOwner,
             androidx.lifecycle.Observer{ response ->
                Timber.d(" onViewCreated->viewModel")
                when (response) {
                    is Resource.Success -> {
                        Timber.d(" onViewCreated->Resource.Success")
                        response.data?.let { data ->
                            Timber.d("onViewCreated-> Resource.Error ${data.code}")
                            if (data.status) {
                                setTime(300000)
                                startTimer()
                                txtResendCode.visibility = View.INVISIBLE
                            }
                        }
                    }
                    is Resource.Error -> {
                        Timber.d("onViewCreated-> Resource.Error ${response.message!!}")
                    }
                    is Resource.Loading -> {
                        Timber.d("onViewCreated-> Resource.Loading")
                    }
                }
            })


        btnVerify.setOnClickListener {
            val activation_code = squareField.text.toString()
            if (activation_code.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى إدخال رمز التأكيد", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.activateAccount(ActivateAccount(activation_code))
        }


        txtResendCode.setOnClickListener {
            viewModel.resendActivation()
        }

    }


    override fun onResume() {
        super.onResume()
        setTime(300000)
        startTimer()

    }

    private fun setTime(milliseconds: Long) {
        mStartTimeInMillis = milliseconds
        resetTimer()
    }


    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
                txtResendCode.visibility = View.VISIBLE
            }
        }.start()
        mTimerRunning = true

    }

    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
    }

    private fun updateCountDownText() {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String?
        timeLeftFormatted = if (hours > 0) {
            java.lang.String.format(
                Locale.getDefault(),
                "%d:%02d:%02d", hours, minutes, seconds
            )
        } else {
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        }
        txtDownTimer.text = timeLeftFormatted ?: ""
    }


    override fun onStop() {
        requireActivity().finish()
        mCountDownTimer!!.cancel()
        super.onStop()
    }

}