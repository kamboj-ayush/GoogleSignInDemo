package com.example.googlesignindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.googlesignindemo.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnSignInGoogle.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Start the ProfileActivity and pass user information as extras
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("name", account?.displayName)
            intent.putExtra("email", account?.email)
            intent.putExtra("photoUrl", account?.photoUrl?.toString())
            startActivity(intent)

        } catch (e: ApiException) {
            // Handle sign-in failure
            Log.e("GoogleSignIn", "signInResult: failed code=${e.statusCode}")
            updateUI(null)
        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // Handle the signed-in user (e.g., display user information)
            val displayName = account.displayName
            val email = account.email
            // ...

            // If needed, you can update your UI elements here
        } else {
            // Handle the sign-out or failed sign-in
        }
    }



    companion object {
        private const val RC_SIGN_IN = 123
    }
}


