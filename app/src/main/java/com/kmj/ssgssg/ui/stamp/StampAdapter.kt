package com.kmj.ssgssg.ui.stamp

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kmj.ssgssg.databinding.ItemRecyclerStampBinding

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
    init {
        lastClickedPosition= -1
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
                if(lastClickedPosition!=adapterPosition){
                    lastClickedPosition = adapterPosition
                    lastClicked = binding.recylerItemStampImage

                    binding.recylerItemStampImage.setPadding(0,0,0,0)
                    binding.recylerItemStampImage.setImageResource(getItem(adapterPosition).stamp96ID)
                }
                else{
                    lastClickedPosition=-1
                }

            }
        }

        fun onBindView(item: StampData) {
            if(adapterPosition!= lastClickedPosition){
                binding.recylerItemStampImage.setImageResource(item.stampID)
                binding.recylerItemStampImage.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#999999"))
            }
            else{
                binding.recylerItemStampImage.setImageResource(item.stamp96ID)
                binding.recylerItemStampImage.clearColorFilter()
            }

        }
    }
}
