package com.nurbk.ps.homebusness.util

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nurbk.ps.homebusness.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import timber.log.Timber


object Constant {

    const val TYPE_STORE = "typeStore"
    const val DETAILS_PRODUCT = "details_product"
    const val DETAILS_STORE = "details_store"
    const val CALL = "CALL"
    const val USERNAME = "USERNAME"
    const val LANG = "LANG"
    const val TELEPHONE = "telephone"
    const val TOKEN = "token"
    const val START = "start"
    const val LOGIN = "login"
    const val NAME = "name"
    const val PHONE_NUMBER = "phoneNumber"
    const val MARKET_ID = "marketId"
    const val TYPE = "TYPE"
    const val DATA_PROFILE = "data_profile"
    const val SEARCH = "search"
    const val REQUEST_IMAGE_CODE = 1
    const val EDITADDRESS = "editAddress"
    const val NoteficationStatus = "enable"

    fun getSharePref(context: Context) =
        context.getSharedPreferences("Share", Activity.MODE_PRIVATE)

    fun editor(context: Context) = getSharePref(context).edit()


    lateinit var dialog: Dialog
    fun showDialog(activity: Activity) {
        dialog = Dialog(activity).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
        }
        dialog.show()
    }


    fun setImage(context: Context, url: Any?, iv: ImageView, ivPaceHolder: Int) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .placeholder(ivPaceHolder)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(iv)
    }


    fun setUpStatusBar(activity: Activity, types: Int) {
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (types == 1) {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.colorPrimary)
        } else {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
        }
    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }


    fun permissionImger(context: Context, onPermissionsChecked: () -> Unit) {
        Timber.d(" Add Image")
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            onPermissionsChecked()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {

                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {

            }
            .check()
    }


    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }
}