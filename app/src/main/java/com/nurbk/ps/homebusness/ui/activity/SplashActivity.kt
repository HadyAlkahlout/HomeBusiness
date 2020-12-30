package com.nurbk.ps.homebusness.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ActivitySplashBinding
import com.nurbk.ps.homebusness.ui.fragment.dialog.ProblemDialogFragment
import com.nurbk.ps.homebusness.ui.fragment.dialog.UpdateDialogFragment
import com.nurbk.ps.homebusness.ui.viewmodel.auth.SplashState
import com.nurbk.ps.homebusness.ui.viewmodel.auth.SplashViewModel
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

class SplashActivity : AppCompatActivity(), UpdateDialogFragment.GoFragmentMessage,
    ProblemDialogFragment.ProblemFragmentMessage {

    lateinit var mBinding: ActivitySplashBinding
    val viewModel by lazy {
        ViewModelProvider(this)[SplashViewModel::class.java]
    }
    private val share by lazy {
        Constant.getSharePref(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        Constant.setLanguage(share.getString(Constant.LANG, "ar").toString(), this)
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionCode

        viewModel.liveData.observe(this, {
            when (it) {
                is SplashState.MainActivity -> {
                    viewModel.dataStingLiveData.observe(
                        this,
                        Observer { response ->
                            Timber.d(" onViewCreated->viewModel")
                            when (response) {
                                is Resource.Success -> {
                                    Timber.d(" onViewCreated->Resource.Success")
                                    response.data?.let { data ->
                                        Log.e("tttttcode", "${data.code}")
                                        if (data.status) {
                                            if ((data.data.forceUpdate == "yes" || data.data.forceUpdate == "android")
                                                && data.data.android_version != version.toString()
                                            ) {
                                                UpdateDialogFragment(
                                                    this
                                                ).show(supportFragmentManager, "")
                                            } else if (data.data.forceClose == "yes" || data.data.forceClose == "android") {
                                                ProblemDialogFragment(
                                                    this
                                                ).show(supportFragmentManager, "")
                                            } else if ((data.data.forceUpdate == "no" && data.data.forceClose == "no")
                                                && data.data.android_version == version.toString()
                                            ) {
                                                goToMainActivity()
                                            }
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
            }
        })


        val a: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        a.reset()

        imgSplashLogo.clearAnimation()
        imgSplashLogo.startAnimation(a)
    }


    private fun goToMainActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    override fun onClickGo() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }

    override fun onClickProblem() {
        finish()
    }
}