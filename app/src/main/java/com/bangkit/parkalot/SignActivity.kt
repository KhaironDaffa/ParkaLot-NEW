package com.bangkit.parkalot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.parkalot.databinding.ActivitySignBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignActivity : AppCompatActivity() {

    private lateinit var signBinding: ActivitySignBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        signBinding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(signBinding.root)

        supportActionBar?.hide()

        signBinding.buttonLog.setOnClickListener {
            if (signBinding.email.text.trim().toString().isEmpty() && signBinding.password.text.trim().toString().isEmpty()) {
                Toast.makeText(this@SignActivity, "Email & Password Required", Toast.LENGTH_SHORT).show()
            }

            else if (signBinding.email.text.trim().toString().isEmpty() && signBinding.password.text.trim().toString().isNotEmpty()) {
                Toast.makeText(this@SignActivity, "Email Required", Toast.LENGTH_SHORT).show()
            }

            else if (signBinding.email.text.trim().toString().isNotEmpty() && signBinding.password.text.trim().toString().isEmpty()){
                Toast.makeText(this@SignActivity, "Password Required", Toast.LENGTH_SHORT).show()
            }

            else {
                signIn(signBinding.email.text.trim().toString(), signBinding.password.text.trim().toString())
            }
        }

        signBinding.signUp.setOnClickListener {
            val intent = Intent(this@SignActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                    Intent(this@SignActivity, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }

                else {
                    Toast.makeText(this@SignActivity, "Authentication Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {
        Intent(this, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }
}