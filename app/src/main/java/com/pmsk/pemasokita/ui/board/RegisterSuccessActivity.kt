package com.pmsk.pemasokita.ui.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.databinding.ActivityRegisterSuccessBinding
import com.pmsk.pemasokita.ui.HomeActivity

class RegisterSuccessActivity : AppCompatActivity() {
    private val binding: ActivityRegisterSuccessBinding by lazy {
        ActivityRegisterSuccessBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnExplore.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}