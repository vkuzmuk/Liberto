package com.vlkuzmuk.freedomcry.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.registration.LoginActivity
import com.vlkuzmuk.freedomcry.databinding.ActivityMainBinding
import com.vlkuzmuk.freedomcry.fragments.NewsFragment
import com.vlkuzmuk.freedomcry.fragments.ProfileFragment
import com.vlkuzmuk.freedomcry.utilits.APP_ACTIVITY
import com.vlkuzmuk.freedomcry.utilits.initFirebase
import com.vlkuzmuk.freedomcry.utilits.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this
        initFirebase()
        replaceFragment(NewsFragment())
        bottomNavigationView()
    }

    private fun bottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNews ->
                    supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.data_container, NewsFragment())
                    .commit()

                R.id.bottomProfile ->
                    supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.data_container, ProfileFragment())
                        .commit()

                R.id.bottomCreatePost -> {
                    val intent = Intent(this, CreateEventActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

}