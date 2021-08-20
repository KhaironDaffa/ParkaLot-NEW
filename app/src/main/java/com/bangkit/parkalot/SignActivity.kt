package com.bangkit.parkalot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.parkalot.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var signBinding: ActivitySignBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientId))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


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

        signBinding.googleButton.setOnClickListener {
            googleSignIn()
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

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignActivity", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }

                else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignActivity", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
}