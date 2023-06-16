package com.pmsk.pemasokita.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmsk.pemasokita.data.Products
import com.pmsk.pemasokita.databinding.FragmentCartBinding
import com.pmsk.pemasokita.ui.adapter.ProductsAdapter

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartItems = mutableListOf<Products>()
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.rvcartItems

        adapter = ProductsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.setItems(cartItems)
    }

    fun addItemToCart(product: Products) {
        cartItems.add(product)
        adapter.setItems(cartItems)
    }
}
