package com.pmsk.pemasokita.ui.cart

import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.data.Products
import com.pmsk.pemasokita.ui.board.EmptyCartFragment

class CartScreenActivity : AppCompatActivity() {
    private val cartItems: MutableList<Products> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_screen)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (isCartEmpty()) {
            showEmptyCartFragment()
        } else {
            showCartFragment()
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val titleTextView = toolbar.findViewById<TextView>(R.id.toolbarTitle)
        titleTextView.text = getString(R.string.belanjaan)
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        titleTextView.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold))
        titleTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun showEmptyCartFragment() {
        val emptyCartFragment = EmptyCartFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, emptyCartFragment)
            .commit()
    }

    private fun showCartFragment() {
        val cartFragment = CartFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, cartFragment)
            .commit()
    }


    private fun isCartEmpty(): Boolean {
        return cartItems.isEmpty()
    }
}
