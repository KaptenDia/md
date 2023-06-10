package com.pmsk.pemasokita.ui.board

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pmsk.pemasokita.databinding.ActivityEmptyCartBinding

class EmptyCartActivity : AppCompatActivity() {
private val binding:ActivityEmptyCartBinding by lazy{
    ActivityEmptyCartBinding.inflate(layoutInflater)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playPropertyAnimation()
        binding.btnBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun playPropertyAnimation() {
        ObjectAnimator.ofFloat(binding.ivDoodle2,View.TRANSLATION_X, -50f, 80f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
    }