package com.kmj.ssgssg.ui.stamp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kmj.ssgssg.databinding.ItemRecyclerStampBinding

class StampAdapter(
    private val makeNewStamp: (Int) -> Unit,
    private val removeStamp: (Int) -> Unit,
) : ListAdapter<StampData, RecyclerView.ViewHolder>(diffutil) {
    var isClickable: Boolean = true
    var currentSelectedPosition: Int = -1

    companion object {

        val diffutil = object : DiffUtil.ItemCallback<StampData>() {
            override fun areItemsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem.stampTag == newItem.stampTag

            override fun areContentsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem == newItem

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        StampViewHolder(
            ItemRecyclerStampBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = getItem(position)
        (holder as StampViewHolder).onBindView(item)
    }

    inner class StampViewHolder(val binding: ItemRecyclerStampBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.recylerItemStampImage.setOnTouchListener { v, event ->
                if (isClickable) {

                    when (event.action) {

                        MotionEvent.ACTION_CANCEL -> {
                            if (currentSelectedPosition != adapterPosition)
                                binding.recylerItemStampImage.setColorFilter(null)
                        }
                        MotionEvent.ACTION_DOWN -> {
                            if (currentSelectedPosition != adapterPosition)
                                binding.recylerItemStampImage.setColorFilter(
                                    Color.argb(51, 6, 6, 6)
                                )

                        }
                    }
                }

                false
            }

            binding.recylerItemStampImage.setOnClickListener {
                if (isClickable) {
                    currentSelectedPosition = adapterPosition
                    binding.recylerItemStampImage.setColorFilter(Color.argb(127, 255, 255, 255))
                    makeNewStamp.invoke(adapterPosition)
                }
            }


        }

        fun onBindView(item: StampData) {

            binding.recylerItemStampImage.apply {
                setImageResource(item.stampID)

            }


            if (currentSelectedPosition == adapterPosition){
                binding.recylerItemStampImage.setColorFilter(Color.argb(127, 255, 255, 255))

            }
            else {
                binding.recylerItemStampImage.setColorFilter(null)
            }


        }
    }
}
