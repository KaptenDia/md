package com.pmsk.pemasokita.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.databinding.ActivitySplashScreenBinding
import com.pmsk.pemasokita.ui.auth.LoginScreenActivity
import com.pmsk.pemasokita.utils.SharedPrefManager

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val binding: ActivitySplashScreenBinding by lazy{
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val splashTimeout: Long = 2000
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        setupView()
        Handler().postDelayed({
            checkLoginState()
        }, splashTimeout)

    }

    private fun checkLoginState() {
        val isLoggedIn = SharedPrefManager.isLoggedIn(this)

        if (isLoggedIn && auth.currentUser != null) {
            navigateToHome()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToHome() {
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout)
    }

    private fun navigateToLogin() {
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, LoginScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}