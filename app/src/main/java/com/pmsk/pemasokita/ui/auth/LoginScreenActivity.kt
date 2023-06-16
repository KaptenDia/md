package com.pmsk.pemasokita.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.databinding.ActivityLoginScreenBinding
import com.pmsk.pemasokita.ui.HomeActivity
import com.pmsk.pemasokita.utils.SharedPrefManager
import com.pmsk.pemasokita.viewmodels.LoginViewModel

class LoginScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivityLoginScreenBinding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val progress = binding.loading
        progress.visibility = View.GONE
        supportActionBar?.hide()
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (email.isEmpty()) {
                binding.editEmail.error = "Email wajib diisi"
                binding.editEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editEmail.error = "Email tidak valid"
                binding.editEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.editPassword.error = "Password wajib diisi"
                binding.editPassword.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 8) {
                binding.editPassword.error = "Password harus lebih dari 8"
                binding.editPassword.requestFocus()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE

            loginViewModel.loginUser(email, password,
                onSuccess = {
                    SharedPrefManager.setLoggedIn(this, true)
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Login Berhasil !!" , Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                }
            )
        }

        auth = FirebaseAuth.getInstance()
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnLoginWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    val idToken = it.idToken
                    val displayName = it.displayName
                    val email = it.email
                    val progress = binding.loading
                    progress.visibility = View.GONE
                    loginViewModel.signInWithGoogle(idToken, displayName, email,
                        onSuccess = {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            if (userId !=null) {
                                val databaseReference = FirebaseDatabase.getInstance().reference
                                    .child("users")
                                    .child(userId)
                                databaseReference.child("username").setValue(displayName)
                            }
                            SharedPrefManager.setLoggedIn(this, true)
                            startActivity(Intent(this, HomeActivity::class.java))
                            Toast.makeText(this, "Yeayyy,Login Dengan Akun Google Berhasil!!!.", Toast.LENGTH_SHORT).show()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE
                        }
                    )
                }
            } catch (e: ApiException) {
                print(e.toString())
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}

