package com.vlkuzmuk.freedomcry.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventsActivity
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.databinding.ActivitySettingsBinding
import com.vlkuzmuk.freedomcry.utilits.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private val dbManager: DbManager = DbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initFirebase()
        getDataFromProfile()

        binding.btnSaveFullName.setOnClickListener {
            dbManager.saveSettingsData(this)
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDataFromProfile() {
        val fullname = intent.getStringExtra("fullname").toString()
        if (intent.hasExtra("fullname")) binding.edFullname.setText(fullname)
    }

}