package com.aaqibkhan.dottchallengeaaqib.util

import android.widget.Toast
import com.aaqibkhan.dottchallengeaaqib.App

object UIUtil {

    fun showToast(message: String?) =
        message?.let { Toast.makeText(App.appContext, it, Toast.LENGTH_SHORT).show() }

}