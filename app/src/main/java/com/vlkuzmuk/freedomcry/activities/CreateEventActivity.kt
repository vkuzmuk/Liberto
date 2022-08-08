package com.vlkuzmuk.freedomcry.activities

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.databinding.ActivityCreateEventBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.*
import java.io.ByteArrayOutputStream


class CreateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var event: EventModel
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListeners()
        downloadUserInfo()
        selectImage()

    }

    private fun onClickListeners() {
        binding.tvLocation.setOnClickListener { setLocationChooser() }
        binding.btnCreatePost.setOnClickListener { createEvent() }
    }

    private fun downloadUserInfo() {
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val username: String =
                    (it.getValue(USER.username::class.java) ?: USER.username).toString()
                binding.tvUsername.text = username
            })
    }

    private fun createEvent() {
        event = EventModel()
        if (binding.chAnonimity.isChecked) event.username = getString(R.string.sir_liberto)
        else {
            event.username = binding.tvUsername.text.toString()
            event.uid = CURRENT_UID
        }
        event.post_text = binding.edTextPost.text.toString()
        event.location = binding.tvLocation.text.toString()
        event.time = System.currentTimeMillis().toString()
        event.key = REF_DATABASE_ROOT.push().key
        if (binding.imageCreatePost.visibility == View.GONE) saveEventToDbWithoutImage()
        else uploadImageToDb()


    }

    private fun setLocationChooser() {
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
        val mBuilder = AlertDialog.Builder(this@CreateEventActivity)
        // set title
        mBuilder.setTitle("Обери локацію")
        // set single choice
        mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
            // set text
            binding.tvLocation.text = listItems[i]
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
    }

    private fun selectImage() {
        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.imageCreatePost.setImageURI(uri)
            imageUri = uri!!
        }
        binding.btnImageChooser.setOnClickListener {
            binding.imageCreatePost.visibility = View.VISIBLE
            loadImage.launch("image/*")
        }
    }

    private fun prepareImage(bitmap: Bitmap): ByteArray {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        return outStream.toByteArray()
    }

    private fun saveEventToDbWithoutImage() {
        event.photoUrl = "empty"
        if (binding.edTextPost.text.isNotEmpty()) {
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .push()
                .child(CURRENT_UID)
                .setValue(event)
                .addOnCompleteListener {
                    finish()
                }
                .addOnFailureListener { e ->
                    showToast(this, e.message.toString())
                }
        } else binding.edTextPost.error = "Ну скажіть хоть щось, будь ласка =)"
    }

    private fun uploadImageToDb() {
        val byteArray = prepareImage(binding.imageCreatePost.drawToBitmap())
        uploadImage(byteArray) {
            event.photoUrl = it.result.toString()
                if (binding.edTextPost.text.isNotEmpty()) {
                REF_DATABASE_ROOT
                    .child(NODE_EVENTS)
                    .push()
                    .child(CURRENT_UID)
                    .setValue(event)
                    .addOnSuccessListener {
                        finish()
                    }
                    .addOnFailureListener { e ->
                        showToast(this, e.message.toString())
                    }
            } else binding.edTextPost.error = "Ну скажіть хоть щось, будь ласка =)"
        }
    }

    private fun uploadImage(byteArray: ByteArray, listener: OnCompleteListener<Uri>) {
        val storageRef = Firebase.storage.reference
        val imStorageRef =
            storageRef
            .child(NODE_POST_IMAGES)
            .child(CURRENT_UID)
            .child("image_${System.currentTimeMillis()}")
        val uploadTask = imStorageRef.putBytes(byteArray)
        uploadTask.continueWithTask {
            imStorageRef.downloadUrl
        }.addOnCompleteListener(listener)
    }


}