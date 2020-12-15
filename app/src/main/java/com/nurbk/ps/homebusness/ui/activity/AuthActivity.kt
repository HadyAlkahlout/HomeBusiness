package com.nurbk.ps.homebusness.ui.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nurbk.ps.homebusness.databinding.ActivityAuthBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.util.Constant
import java.util.*

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


       setLanguage(share.getString(Constant.LANG, "ar").toString(), this)


    }


    companion object {
        fun setLanguage(lan: String, context: Context) {
            val res = context.resources
            val dr = res.displayMetrics
            val cr = res.configuration
            cr.setLocale(Locale(lan))
            res.updateConfiguration(cr, dr)
        }
    }


}