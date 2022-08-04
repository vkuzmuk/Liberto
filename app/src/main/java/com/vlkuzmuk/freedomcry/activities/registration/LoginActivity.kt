package com.vlkuzmuk.freedomcry.activities.registration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventActivity
import com.vlkuzmuk.freedomcry.databinding.LoginPageBinding
import com.vlkuzmuk.freedomcry.utilits.AUTH
import com.vlkuzmuk.freedomcry.utilits.initFirebase
import com.vlkuzmuk.freedomcry.utilits.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding
    //private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        getDataFromRegister()
        setContentView(binding.root)
        initFirebase()
        checkUser()
        onClick()

    }



    private fun onClick() {
        // if user doesn't have an account
        binding.tvToRegister.setOnClickListener {
            email = binding.edEmail.text.toString().trim()
            password = binding.edPassword.text.toString().trim()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("emailL", email)
            intent.putExtra("passwordL", password)
            startActivity(intent)
            finish()
        }

        // login
        binding.btnLogin.setOnClickListener {
            // validate data
            validateData()
        }

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
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.edEmail.error = getString(R.string.invalid_email)
        } else if (TextUtils.isEmpty(password)) {
            // no password entered
            binding.edPassword.error = getString(R.string.please_enter_password)
        } else {
            // data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        AUTH.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // open profile
                startActivity(Intent(this, EventActivity::class.java))
            }.addOnFailureListener { e ->
                // login failed
                showToast(this, "${getString(R.string.authorization_failed)} ${e.message}")
            }
    }


    private fun checkUser() {
        // if user already logged in go to news page
        val firebaseUser = AUTH.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, EventActivity::class.java))
            finish()
        }
    }

    private fun getDataFromRegister() {
        val emailFromLogin = intent.getStringExtra("emailR").toString()
        val passwordFromLogin = intent.getStringExtra("passwordR").toString()

        if (intent.hasExtra("emailR")) binding.edEmail.setText(emailFromLogin)
        if (intent.hasExtra("passwordR")) binding.edPassword.setText(passwordFromLogin)


    }


}