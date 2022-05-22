package com.example.drwaing.view.draw

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.example.drwaing.R

object Palette {
    private fun Resources.getColorCompat(resId: Int) = ResourcesCompat.getColor(this, resId, null)

    fun getColor(
        resId: Int,
        resources: Resources? = UIKit.getResources()
    ): Int {
        if (resources == null) return 0
        return resources.getColorCompat(resId)
    }


    val orange get() = getColor(R.color.orange)
    val beige get() = getColor(R.color.beige)
    val blue get() = getColor(R.color.blue)
    val brown get() = getColor(R.color.brown)
    val darkBlue get() = getColor(R.color.dark_blue)
    val green get() = getColor(R.color.green)
    val purple get() = getColor(R.color.purple)
    val red get() = getColor(R.color.red)
    val skyBlue get() = getColor(R.color.sky_blue)
    val white get() = getColor(R.color.white)
    val yellow get() = getColor(R.color.yellow)
    val black get() = getColor(R.color.black)
    val border get() = getColor(R.color.border)
}