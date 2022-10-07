package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.SettingsActivity
import com.vlkuzmuk.freedomcry.activities.registration.LoginActivity
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.databinding.ActivityProfileBinding
import com.vlkuzmuk.freedomcry.utilits.PAGE_PROFILE
import com.vlkuzmuk.freedomcry.utilits.bottomNav

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val dbManager: DbManager = DbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav(this, this, PAGE_PROFILE)
        dbManager.initProfile(binding)
        onClick()
        updateData()
    }

    private fun updateData() = with(binding) {
        refreshPage.setColorSchemeResources(R.color.blue_main)
        refreshPage.setOnRefreshListener {
            dbManager.initProfile(binding)
            refreshPage.isRefreshing = false
        }
    }

    private fun onClick() {
        val auth = FirebaseAuth.getInstance()
        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnToSettings.setOnClickListener {
            val fullname = binding.tvUserFullname.text.toString()
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("fullname", fullname)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()
        getDataFromSettings()
    }

    private fun getDataFromSettings() {
        val newFullname = intent.getStringExtra("newFullname").toString()
        Log.d("MyLog", newFullname)
    }

}