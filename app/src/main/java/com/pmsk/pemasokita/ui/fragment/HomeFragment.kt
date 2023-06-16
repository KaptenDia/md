package com.pmsk.pemasokita.ui.fragment

import com.pmsk.pemasokita.ui.detail.DetailProductActivity
import PaginateProducts
import com.pmsk.pemasokita.data.PopularProductsManager
import com.pmsk.pemasokita.ui.adapter.ProductsAdapter
import RecommendedProductsManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmsk.pemasokita.data.Products
import com.pmsk.pemasokita.databinding.FragmentHomeBinding
import com.pmsk.pemasokita.ui.cart.CartScreenActivity

class HomeFragment : Fragment(), PaginateProducts.LoadingListener, ProductsAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendedProductsAdapter: ProductsAdapter
    private lateinit var popularProductsAdapter: ProductsAdapter
    private lateinit var popularProductsManager: PopularProductsManager
    private lateinit var recommendedProductsManager: RecommendedProductsManager
    private var isLoadingMoreRecommended = false
    private var isLastPageRecommended = false
    private var isLoadingMorePopular = false
    private var isLastPagePopular = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivCart.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), CartScreenActivity::class.java
                )
            )
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return true
            }
        })


        popularProductsAdapter = ProductsAdapter()
        popularProductsManager = PopularProductsManager(popularProductsAdapter, this)

        val popularLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPopularItems.layoutManager = popularLayoutManager
        binding.rvPopularItems.adapter = popularProductsAdapter

        binding.rvPopularItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoadingMorePopular && !isLastPagePopular) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoadingMorePopular = true
                        popularProductsManager.loadMorePopularItems()
                    }
                }
            }
        })
        recommendedProductsAdapter = ProductsAdapter()
        recommendedProductsManager = RecommendedProductsManager(recommendedProductsAdapter, this)

        val recommendedLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedItems.layoutManager = recommendedLayoutManager
        binding.rvRecommendedItems.adapter = recommendedProductsAdapter

        binding.rvRecommendedItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoadingMoreRecommended && !isLastPageRecommended) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoadingMoreRecommended = true
                        recommendedProductsManager.loadMoreRecommendedItems()
                    }
                }
            }
        })
        popularProductsManager.loadPopularItems()
        recommendedProductsManager.loadRecommendedItems()
        popularProductsAdapter.setOnItemClickListener(this)
        recommendedProductsAdapter.setOnItemClickListener(this)
    }

    override fun onLoadingStarted() {
        binding.progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLoadingFinished() {
        binding.progressBar.visibility = View.GONE
        popularProductsAdapter.notifyDataSetChanged()
        recommendedProductsAdapter.notifyDataSetChanged()
        isLoadingMorePopular = false
        isLoadingMoreRecommended = false
    }

    override fun onItemClick(item: Products) {
        val intent = Intent(requireContext(), DetailProductActivity::class.java)
        intent.putExtra("product_name", item.product_name)
        intent.putExtra("product_price", item.product_price)
        intent.putExtra("product_image", item.product_image)
        intent.putExtra("product_amount", item.product_amount)
        intent.putExtra("product_description", item.product_description)
        startActivity(intent)
    }

    private fun performSearch(query: String) {
        val filteredRecommendedProducts = recommendedProductsManager.searchProducts(query)
        val filteredPopularProducts = popularProductsManager.searchProducts(query)

        recommendedProductsAdapter.setItems(filteredRecommendedProducts)
        popularProductsAdapter.setItems(filteredPopularProducts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
