package com.vlkuzmuk.freedomcry.utilits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.ChatActivity
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventManagerActivity
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventsActivity
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.ProfileActivity

fun replaceActivity(context: Context, currentActivity: Activity, activity: Activity) {
    val i = Intent(context, activity :: class.java)
    currentActivity.startActivity(i)
    currentActivity.finish()
    currentActivity.overridePendingTransition(0,0)
}

fun bottomNav(context: Context, activity: Activity, currentPage: Int) {
    val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    var requiredPage = 0
    when (currentPage) {
        1 -> { requiredPage = R.id.bottomEvents }
        2 -> { requiredPage = R.id.bottomEventManager }
        3 -> { requiredPage = R.id.bottomChats }
        4 -> { requiredPage = R.id.bottomProfile }
    }
    bottomNav.selectedItemId = requiredPage
    bottomNav.setOnItemSelectedListener { item ->
        when(item.itemId) {
            R.id.bottomChats -> {
                replaceActivity(context, activity, ChatActivity())
            }
            R.id.bottomProfile -> {
                replaceActivity(context, activity, ProfileActivity())
            }
            R.id.bottomEventManager -> {
                replaceActivity(context, activity, EventManagerActivity())
            }
            R.id.bottomEvents -> {
                replaceActivity(context, activity, EventsActivity())
            }
        }
        true
    }

}

fun showToast(activity: Activity, message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

/*private fun setLocationChooser(activity: Activity) {
    val listItems =
        arrayOf(
            "Гуртожиток №1",
            "Гуртожиток №2",
            "Гуртожиток №2",
            "Гуртожиток №3",
            "Гуртожиток №4",
            "Гуртожиток №5",
            "Гуртожиток №6",
            "Гуртожиток №7"
        )
    val mBuilder = AlertDialog.Builder(activity)
    // set title
    mBuilder.setTitle("Обери локацію")
    // set single choice
    mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
        // set text
        //binding.tvLocation.text = listItems[i]
        // dismiss dialog
        dialogInterface.dismiss()
    }
    // set neutral/cancel button
    mBuilder.setNeutralButton("Скасувати") { dialog, _ ->
        // just dismiss the alertdialog
        dialog.cancel()
    }
    // create and shod dialog
    val mDialog = mBuilder.create()
    mDialog.show()
}*/
