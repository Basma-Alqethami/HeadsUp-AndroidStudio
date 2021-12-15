package com.example.headsup

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_row.view.*

class RVAdapter (private var celebrities: ArrayList<CelebritiesItem>): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val celebrity = celebrities[position]

        holder.itemView.apply {
            tvName.text = celebrity.name
            tvTaboo1.text = celebrity.taboo1
            tvTaboo2.text = celebrity.taboo2
            tvTaboo3.text = celebrity.taboo3
        }
    }

    override fun getItemCount() = celebrities.size
}