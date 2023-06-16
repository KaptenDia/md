package com.pmsk.pemasokita.ui.detail

import ProductViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.data.Products
import com.pmsk.pemasokita.ui.cart.CartFragment

class DetailProductActivity : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private var cartFragment: CartFragment? = null

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val productAmount = intent.getStringExtra("product_amount")
        val productImage = intent.getStringExtra("product_image")
        val productDescription = intent.getStringExtra("product_description")
        val imageView = findViewById<ImageView>(R.id.backButton)

        imageView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val ivItem = findViewById<ImageView>(R.id.ivItem)
        Glide.with(this).load(productImage).into(ivItem)
        findViewById<TextView>(R.id.tvItemName).text = productName
        findViewById<TextView>(R.id.tvItemPrice).text = productPrice
        findViewById<TextView>(R.id.tvItemAmount).text = "/$productAmount"
        findViewById<TextView>(R.id.itemDescription).text = productDescription

        val lyDescription = findViewById<LinearLayout>(R.id.lyDescription)
        val itemDescription = findViewById<TextView>(R.id.itemDescription)

        lyDescription.setOnClickListener {
            if (itemDescription.visibility == View.GONE) {
                itemDescription.visibility = View.VISIBLE
            } else {
                itemDescription.visibility = View.GONE
            }
        }

        val addToCartButton = findViewById<Button>(R.id.btn_addToCart)

        addToCartButton.setOnClickListener {
            val selectedProduct = Products(
                productName,
                productPrice,
                productImage,
                productDescription,
                productAmount
            )
            cartFragment?.addItemToCart(selectedProduct)
            Toast.makeText(this@DetailProductActivity, "Item added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CartFragment) {
            cartFragment = fragment
        }
    }
}
