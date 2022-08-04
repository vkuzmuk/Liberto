package com.vlkuzmuk.freedomcry.utilits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vlkuzmuk.freedomcry.R

fun replaceFragment(containerId: Int, fragment: Fragment) {
    APP_ACTIVITY
        .supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(containerId, fragment)
        .commit()
}

fun replaceActivity(activity: Activity, context: Context) {
    val i = Intent(context, activity :: class.java)
    activity.startActivity(i)
    activity.overridePendingTransition(0,0)
}

fun showToast(activity: Activity, message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

