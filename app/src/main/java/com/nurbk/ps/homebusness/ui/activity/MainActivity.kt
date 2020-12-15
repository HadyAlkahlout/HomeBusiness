package com.nurbk.ps.homebusness.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ActivityMainBinding
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.setUpStatusBar
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.model.MyStory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    private val share by lazy {
        Constant.getSharePref(this)
    }
    private val getShare by lazy {
        Constant.getSharePref(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        AuthActivity.setLanguage(share.getString(Constant.LANG, "ar").toString(), this)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        NavigationUI.setupWithNavController(
            mBinding.bottomNav,
            Objects.requireNonNull(navHostFragment).navController
        )
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        NavigationUI.setupWithNavController(
            mBinding.toolbar, navController, appBarConfiguration
        )



        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,

                R.id.categoriesFragment -> {
                    mBinding.bottomNav.visibility = View.VISIBLE
                    setUpStatusBar(this, 0)
                }
                R.id.favoritesFragment,
                        R.id.cartFragment
                ->{
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
                        navController.navigateUp()
                    }else{
                        mBinding.bottomNav.visibility = View.VISIBLE
                        setUpStatusBar(this, 0)
                    }
                }

                else -> {
                    setUpStatusBar(this, 1)
                    mBinding.bottomNav.visibility = View.GONE
                }

            }
        }

    }


}