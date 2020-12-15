package com.nurbk.ps.homebusness.service

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nurbk.ps.homebusness.ui.activity.SplashActivity
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.NoteficationStatus

class MyFirebaseMassagingService : FirebaseMessagingService() {


    private val share by lazy {
        Constant.getSharePref(applicationContext)
    }

    lateinit var myNotificationManager: MyNotificationManager
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }




    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        myNotificationManager = MyNotificationManager(this)

        if (share.getBoolean(NoteficationStatus, false)) {
            if (p0.data.isNullOrEmpty()) {
                myNotificationManager.showNotification(
                        1, p0.notification!!.title!!, p0.notification!!.body!!,
                        Intent(
                                applicationContext,
                                SplashActivity::class.java
                        )
                )
            } else {
                myNotificationManager.showNotification(
                        1, p0.data["title"]!!, p0.data["message"]!!,
                        Intent(
                                applicationContext,
                                SplashActivity::class.java
                        )
                )
            }
        }
    }

}