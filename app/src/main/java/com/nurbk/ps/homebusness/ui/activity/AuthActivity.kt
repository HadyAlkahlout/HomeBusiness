package com.nurbk.ps.homebusness.ui.activity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nurbk.ps.homebusness.databinding.ActivityAuthBinding
import com.nurbk.ps.homebusness.util.Constant

class AuthActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityAuthBinding
    private val share by lazy {
        Constant.getSharePref(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        Constant.setLanguage(share.getString(Constant.LANG, "ar").toString(), this)
    }


}