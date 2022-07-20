package com.vlkuzmuk.freedomcry.activities.registration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.databinding.RegisterPageBinding
import com.vlkuzmuk.freedomcry.utilits.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterPageBinding

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFirebase()
        getDataFromLogin()
        onClick()


    }

    private fun onClick() {

        binding.tvToLogin.setOnClickListener {
            email = binding.edEmail.text.toString().trim()
            password = binding.edPassword.text.toString().trim()
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("emailR", email)
            intent.putExtra("passwordR", password)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener { validateData() }

        binding.chShowPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.edPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.edPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun validateData() {
        // get data
        email = binding.edEmail.text.toString().trim()
        password = binding.edPassword.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.edEmail.error = getString(R.string.invalid_email)
        } else if (TextUtils.isEmpty(password)) {
            // no password entered
            binding.edPassword.error = getString(R.string.please_enter_password)
        } else if (password.length < 6) {
            binding.edPassword.error = getString(R.string.password_must_be_at_least_6_characters)
        } else {
            firebaseRegister()
        }
    }

    private fun firebaseRegister() {
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                showToast(this, "${getString(R.string.account_created)} $email")
                saveUsernameAsUid()
                // open news page
                startActivity(Intent(this, UsernameActivity::class.java))
                finish()
            }.addOnFailureListener { e ->
                // sign up failed
                showToast(this, "${getString(R.string.registration_failed)} ${e.message}")
            }
    }

    private fun saveUsernameAsUid() {
        CURRENT_UID = AUTH.currentUser!!.uid
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .setValue(CURRENT_UID)
    }

    private fun getDataFromLogin() {
        val emailFromLogin = intent.getStringExtra("emailL").toString()
        val passwordFromLogin = intent.getStringExtra("passwordL").toString()

        if (intent.hasExtra("emailL")) binding.edEmail.setText(emailFromLogin)
        if (intent.hasExtra("passwordL")) binding.edPassword.setText(passwordFromLogin)


    }
}