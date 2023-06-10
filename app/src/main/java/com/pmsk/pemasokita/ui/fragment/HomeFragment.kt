package com.pmsk.pemasokita.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pmsk.pemasokita.R
import com.pmsk.pemasokita.databinding.FragmentHomeBinding
import com.pmsk.pemasokita.ui.adapter.RecommendedItemAdapter
import com.pmsk.pemasokita.ui.board.EmptyCartActivity

class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() =_binding
    private lateinit var recommendedItem:RecommendedItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view : View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recommendedItem = RecommendedItemAdapter()
        binding?.ivCart?.setOnClickListener{
            startActivity(
                Intent(
                    this@HomeFragment.context,EmptyCartActivity::class.java
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}