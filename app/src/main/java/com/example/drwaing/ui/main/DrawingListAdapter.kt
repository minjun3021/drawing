package com.example.drwaing.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drwaing.R

/**
 * ListAdapter + DiffUtil 공부하기
 * DiffUtil.ItemCallback에 있는 두가지 메소드 (itemSame/contentsSame) 차이 공부하기 면접에서 많이 물어봄
 *
 * ViewType 공부하기
 */
class DrawingListAdapter : ListAdapter<DrawingListData, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawingListData>() {
    // TODO : 아이템 아이디로 같은지 여부 판단
    override fun areItemsTheSame(oldItem: DrawingListData, newItem: DrawingListData): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: DrawingListData, newItem: DrawingListData): Boolean = oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        // TODO : Header xml 구현
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