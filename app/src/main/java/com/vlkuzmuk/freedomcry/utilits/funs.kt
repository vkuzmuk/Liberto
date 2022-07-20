package com.vlkuzmuk.freedomcry.utilits

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vlkuzmuk.freedomcry.R

fun replaceFragment(fragment: Fragment) {
    APP_ACTIVITY
        .supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(R.id.data_container, fragment)
        .commit()
}

fun showToast(activity: Activity, message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

