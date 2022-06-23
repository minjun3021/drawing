package com.example.drwaing.ui.stamp

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drwaing.R
import com.example.drwaing.data.diary.Stamp
import com.example.drwaing.databinding.ItemRecyclerStampBinding

class StampAdapter : ListAdapter<StampData, RecyclerView.ViewHolder>(diffutil) {

    companion object {

        val diffutil = object : DiffUtil.ItemCallback<StampData>() {
            override fun areItemsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem.stampID == newItem.stampID

        }
        lateinit var lastClicked: ImageView
        var lastClickedPosition: Int = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        StampViewHolder(ItemRecyclerStampBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = getItem(position)
        (holder as StampViewHolder).onBindView(item)
    }

    inner class StampViewHolder(val binding: ItemRecyclerStampBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.recylerItemStampImage.setOnClickListener {
                if (lastClickedPosition > -1) {
                    lastClicked.setPadding(0,8,0,8)
                    lastClicked.setImageResource(getItem(lastClickedPosition).stampID)
                }
                lastClickedPosition = adapterPosition
                lastClicked = binding.recylerItemStampImage

                binding.recylerItemStampImage.setPadding(0,0,0,0)
                binding.recylerItemStampImage.setImageResource(getItem(adapterPosition).stamp96ID)
            }
        }

        fun onBindView(item: StampData) {
            if(adapterPosition!= lastClickedPosition){
                binding.recylerItemStampImage.setImageResource(item.stampID)
            }
            else{
                binding.recylerItemStampImage.setImageResource(item.stamp96ID)
            }

        }
    }
}
