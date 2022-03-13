package com.example.drwaing.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drwaing.R

class DrawingListAdapter : ListAdapter<DrawingListData, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawingListData>() {
    override fun areItemsTheSame(oldItem: DrawingListData, newItem: DrawingListData): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: DrawingListData, newItem: DrawingListData): Boolean = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        1 -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_drawing, parent, false))
        else -> ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_drawing, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {

            }

            is HeaderViewHolder -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DrawingListData.DrawingData -> 0
        is DrawingListData.Header -> 1
    }

    class ListViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    class HeaderViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}