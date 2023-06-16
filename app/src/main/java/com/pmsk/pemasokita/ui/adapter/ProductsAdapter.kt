package com.pmsk.pemasokita.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.pmsk.pemasokita.data.Products
import com.pmsk.pemasokita.databinding.CartItemsListBinding
import com.pmsk.pemasokita.databinding.RecommendedItemListBinding
import com.pmsk.pemasokita.databinding.PopularItemListBinding

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val itemList: MutableList<Products> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var productList: List<Products> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            TYPE_RECOMMENDED -> RecommendedItemListBinding.inflate(inflater, parent, false)
            TYPE_POPULAR -> PopularItemListBinding.inflate(inflater, parent, false)
            TYPE_CART -> CartItemsListBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
        return when (item.product_category) {
            "recommended" -> TYPE_RECOMMENDED
            "popular" -> TYPE_POPULAR
            "cart" -> TYPE_CART
            else -> super.getItemViewType(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Products>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Products>) {
        val startPosition = itemList.size
        itemList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }


    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Products) {
            when (binding) {
                is RecommendedItemListBinding -> {
                    Glide.with(itemView)
                        .load(item.product_image)
                        .into(binding.ivItem)
                    binding.tvItemName.text = item.product_name
                    binding.tvItemPrice.text = item.product_price
                }
                is PopularItemListBinding -> {
                    Glide.with(itemView)
                        .load(item.product_image)
                        .into(binding.ivPopularItem)
                    binding.tvPopularItemName.text = item.product_name
                    binding.tvPopularItemPrice.text = item.product_price
                }
                is CartItemsListBinding -> {
                    Glide.with(itemView)
                        .load(item.product_image)
                        .into(binding.ivImage)
                    binding.itemName.text = item.product_name
                    binding.itemPrice.text = item.product_price
                }
            }
        }

    }

    fun submitList(list: List<Products>) {
        productList = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.product_name == newItem.product_name
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TYPE_RECOMMENDED = 1
        private const val TYPE_POPULAR = 2
        private const val TYPE_CART = 3
    }
}
