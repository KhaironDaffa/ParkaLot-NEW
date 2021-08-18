package com.bangkit.parkalot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.parkalot.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        supportActionBar?.hide()

        registerBinding.buttonSignUp.setOnClickListener {
            if (registerBinding.email.text.trim().toString().isEmpty() && registerBinding.password.text.trim().toString().isEmpty()) {
                Toast.makeText(this, "Email & Password Required", Toast.LENGTH_SHORT).show()
            }

            else if(registerBinding.email.text.trim().toString().isEmpty() && registerBinding.password.text.trim().toString().isNotEmpty()) {
                Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show()
            }

            else if(registerBinding.email.text.trim().toString().isNotEmpty() && registerBinding.password.text.trim().toString().isEmpty()) {
                Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show()
            }

            else {
                createAccount(registerBinding.email.text.trim().toString(), registerBinding.password.text.trim().toString())
            }
        }

        registerBinding.logIn.setOnClickListener {
            val intent = Intent(this@RegisterActivity, SignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }

                else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}