package com.example.ingenieriaproyecto

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton

class Bienvenida : AppCompatActivity() {

    companion object {
        private const val PREFS_FILE = "myPrefs"
        private const val PREFS_KEY_LOGGED_IN = "isLoggedIn"
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            navigateToNextActivity()
            return
        }

        val googleSignInButton = findViewById<SignInButton>(R.id.Google)

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account = task.result
                if (account != null) {
                    sharedPreferences.edit().putBoolean(PREFS_KEY_LOGGED_IN, true).apply()
                    navigateToNextActivity()
                }
            } else {
            }
        }

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        googleSignInButton.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, Materias::class.java)
        startActivity(intent)
        finish()
    }
}