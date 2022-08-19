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


    companion object {

        val diffutil = object : DiffUtil.ItemCallback<StampData>() {
            override fun areItemsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem.stampTag == newItem.stampTag

            override fun areContentsTheSame(oldItem: StampData, newItem: StampData) =
                oldItem == newItem

        }

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
        val paddingValue=(8 * binding.root.context.resources.displayMetrics.density).toInt()
        init {

            binding.recylerItemStampImage.setOnClickListener {

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItem(adapterPosition).constantState?.let {
                        removeStamp.invoke(adapterPosition)
                        binding.recylerItemStampImage.apply {
                            setPadding(0, paddingValue, 0, paddingValue)
                            setImageResource(getItem(adapterPosition).stampID)
                            setColorFilter(Color.parseColor("#D3D3D3"), PorterDuff.Mode.MULTIPLY)
                        }

                    }
                        ?: run {
                            makeNewStamp.invoke(adapterPosition)
                            binding.recylerItemStampImage.apply {
                                setImageResource(getItem(adapterPosition).stamp96ID)
                                setPadding(0, 0, 0, 0)
                                colorFilter = null
                            }

                        }


                }


            }
        }

        fun onBindView(item: StampData) {
            item.constantState?.let {
                binding.recylerItemStampImage.apply {
                    setImageResource(item.stamp96ID)
                    setPadding(0, 0, 0, 0)

                    colorFilter = null
                }


            }
                ?: kotlin.run {
                    binding.recylerItemStampImage.apply {
                        setImageResource(item.stampID)
                        setPadding(0, paddingValue, 0, paddingValue)

                        setColorFilter(Color.parseColor("#D3D3D3"), PorterDuff.Mode.MULTIPLY)
                    }
                }

        }
    }
}
