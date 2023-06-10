package com.pmsk.pemasokita.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmsk.pemasokita.R

class RecommendedItemAdapter: RecyclerView.Adapter<RecommendedItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_home, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}