package com.example.drwaing.view.draw

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.drwaing.extension.dp

class ColorPickerAdapter(
    val changedListener: (Int) -> Unit
) : RecyclerView.Adapter<ColorPickerAdapter.ViewHolder>() {

    private val colors = listOf(
        Palette.white,
        Palette.black,
        Palette.red,
        Palette.orange,
        Palette.yellow,
        Palette.green,
        Palette.skyBlue,
        Palette.blue,
        Palette.darkBlue,
        Palette.purple,
        Palette.beige,
        Palette.brown,
    )

    var selectedColor = Palette.black
        set(value) {
            val lastPosition = colors.indexOf(field)
            field = value
            if (lastPosition >= 0) notifyItemChanged(lastPosition)
            notifyItemChanged(colors.indexOf(value))
            changedListener.invoke(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(56.dp.toInt(), 56.dp.toInt())
        }
        return ViewHolder(imageView).apply {
            imageView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                selectedColor = colors[adapterPosition]
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = ColorDrawable(colors[position], colors[position] == Palette.white)
        drawable.selected = selectedColor == colors[position]
        holder.imageView.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int = colors.size

    class ViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}