package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.SettingsActivity
import com.vlkuzmuk.freedomcry.activities.registration.LoginActivity
import com.vlkuzmuk.freedomcry.databinding.ActivityProfileBinding
import com.vlkuzmuk.freedomcry.utilits.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav(this, this, PAGE_PROFILE)
        initProfile()
        onClick()
        updateData()
    }

    private fun updateData() = with(binding) {
        refreshPage.setColorSchemeResources(R.color.blue_main)
        refreshPage.setOnRefreshListener {
            initProfile()
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


    @SuppressLint("SetTextI18n")
    private fun initProfile() {
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val username: String =
                    (it.getValue(USER.username::class.java) ?: USER.username).toString()
                binding.tvUsername.text = "@$username"
            })

        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FULLNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val fullname: String =
                    (it.getValue(USER.fullname::class.java) ?: USER.fullname).toString()
                binding.tvUserFullname.text = fullname
            })
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