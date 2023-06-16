package com.pmsk.pemasokita.ui.scanner

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.pmsk.pemasokita.R

class ScannerResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_result)
        val cardviewDesc = findViewById<CardView>(R.id.cvDescription)
        val btnBack = findViewById<Button>(R.id.btn_back)
        cardviewDesc.visibility = View.GONE

        val ivItem = findViewById<ImageView>(R.id.ivItemResult)
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        ivItem.setImageBitmap(BitmapFactory.decodeFile(imagePath))

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }
    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
    }
}