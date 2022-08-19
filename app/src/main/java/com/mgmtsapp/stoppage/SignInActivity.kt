package com.mgmtsapp.stoppage

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.mgmtsapp.stoppage.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()


        //For full screen
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        binding.signinRegBtn.setOnClickListener {
            binding.signinRegBtn.setTextColor(Color.RED)
            startActivity(Intent(this@SignInActivity, RegisterActivity::class.java))
        }

        binding.forgetPassBtn.setOnClickListener {
            binding.forgetPassBtn.setTextColor(Color.BLACK)
            startActivity(Intent(this@SignInActivity, ForgotPasswordActivity::class.java))
        }


        binding.signinBtn.setOnClickListener {
            val signEmail = binding.signinEmailText.text.toString().trim()
            val signPass = binding.signinPassText.text.toString().trim()
            if (signEmail.isNotEmpty() && signPass.isNotEmpty()) {
                //if email and pass is provided
                firebaseAuth.signInWithEmailAndPassword(signEmail, signPass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                            Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else {
                            Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                //If any field is empty
                if (signEmail.isEmpty() && signPass.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please provide the email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (signPass.isEmpty()) {
                    Toast.makeText(this, "Empty password", Toast.LENGTH_SHORT).show()
                    binding.signinPassText.requestFocus()
                } else if(signEmail.isEmpty()) {
                    Toast.makeText(this, "Empty E-mail address", Toast.LENGTH_SHORT).show()
                    binding.signinEmailText.requestFocus()

                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.signinRegBtn.setTextColor(Color.parseColor("#9A62CC"))
        binding.forgetPassBtn.setTextColor(Color.RED)
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        //For full screen
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}