package com.vlkuzmuk.freedomcry.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.databinding.ActivityCreateEventBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.REF_DATABASE_ROOT


class CreateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
    private var event: EventModel = EventModel()
    private lateinit var imageUri: Uri
    private val dbManager: DbManager = DbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbManager.downloadUserInfo(this, event, true)
        onClickListeners()
        selectImage()

    }

    private fun onClickListeners() {
        switchAnonymity()
        binding.btnCreatePost.setOnClickListener { createEvent() }
    }

    private fun fillEvent() {
        event.title = binding.edTitlePost.text.toString()
        event.text = binding.edTextPost.text.toString()
        event.time = System.currentTimeMillis().toString()
        event.key = REF_DATABASE_ROOT.push().key
    }

    private fun switchAnonymity() = with(binding) {
        swAnonimity.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                binding.tvUsername.text = getString(R.string.username_empty)
                event.username = getString(R.string.username_empty)
            } else {
                dbManager.downloadUserInfo(this@CreateEventActivity, event, false)
            }
        }
    }

    private fun createEvent() {
        fillEvent()
        if (binding.imageCreatePost.visibility == View.GONE)
            event.key?.let { dbManager.saveEventToDbWithoutImage(it, this, event) }
        else
            event.key?.let { dbManager.uploadImageToDb(it, event, this, binding) }


    }

    private fun selectImage() {
        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.imageCreatePost.setImageURI(uri)
            imageUri = uri!!
        }
        binding.btnGallery.setOnClickListener {
            binding.imageCreatePost.visibility = View.VISIBLE
            loadImage.launch("image/*")
        }
    }

}