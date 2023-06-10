package com.pmsk.pemasokita.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.databinding.ActivityRegisterBinding
import com.pmsk.pemasokita.ui.board.RegisterSuccessActivity
import com.pmsk.pemasokita.utils.SharedPrefManager

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageView = findViewById<ImageView>(R.id.backButton)
        val progress = binding.loading
        progress.visibility = View.GONE

        imageView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginScreenActivity::class.java))
            finish()
        }
        binding.btnCreateAccount.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            registerViewModel.registerUser(username, email, password,
                onSuccess = {
                    progress.visibility = View.VISIBLE
                    SharedPrefManager.setLoggedIn(this, true)
                    startActivity(Intent(this, RegisterSuccessActivity::class.java))
                    finish()
                },
                onError = { errorMessage ->
                    progress.visibility = View.GONE
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
