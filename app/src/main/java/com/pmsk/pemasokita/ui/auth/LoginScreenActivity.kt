package com.pmsk.pemasokita.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pmsk.pemasokita.databinding.ActivityLoginScreenBinding
import com.pmsk.pemasokita.ui.HomeActivity
import com.pmsk.pemasokita.ui.board.PaymentSuccessActivity
import com.pmsk.pemasokita.utils.SharedPrefManager
import com.pmsk.pemasokita.viewmodels.LoginViewModel

class LoginScreenActivity : AppCompatActivity() {
    private val binding: ActivityLoginScreenBinding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val progress = binding.loading
        progress.visibility = View.GONE
        supportActionBar?.hide()
        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
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
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                }
            )
        }
        binding.btnLoginWithGoogle.setOnClickListener{
            startActivity(Intent(this,PaymentSuccessActivity::class.java))
        }
    }
}