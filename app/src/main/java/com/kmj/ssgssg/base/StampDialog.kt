package com.kmj.ssgssg.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.kmj.ssgssg.R
import com.kmj.ssgssg.data.diary.DiaryApiModel
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.databinding.DialogConfirmBinding
import com.kmj.ssgssg.databinding.DialogStampBinding
import com.kmj.ssgssg.ui.diary.DiaryActivity
import com.kmj.ssgssg.ui.main.MainFragment
import com.kmj.ssgssg.ui.stamp.StampData
import java.util.ArrayList

class StampDialog(
    context: Context,
    var diary: DiaryApiModel,
    var onPositiveClickListener: () -> Unit = {},
    var onNegativeClickListener: () -> Unit = {},

    ) : Dialog(context, R.style.Day_CustomDialog) {
    private lateinit var binding: DialogStampBinding
    var list: ArrayList<StampData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogStampBinding.inflate(layoutInflater)
        submitStampData()
        binding.dialogStampDate.setText(MainFragment.makeDirayDate(diary.createdDate))
        Glide.with(context)
            .load(diary.imageUrl)
            .into(binding.dialogStampDrawing)
        setIcon()

        setContentView(binding.root)


        binding.dialogStampController.post {
            stamp()
        }


        binding.tvPositive.setOnClickListener {
            onPositiveClickListener.invoke()
            dismiss()

        }
        binding.tvNegative.setOnClickListener { onNegativeClickListener.invoke()
            dismiss()}
    }

    private fun setIcon() {
        when (diary.weather) {
            Weather.SUN.name -> binding.dialogStampWeather.setImageDrawable(ContextCompat.getDrawable(
                context,
                R.drawable.ic_sunny_selected))
            Weather.CLOUD.name -> binding.dialogStampWeather.setImageDrawable(ContextCompat.getDrawable(
                context,
                R.drawable.ic_cloudy_selected))
            Weather.RAIN.name -> binding.dialogStampWeather.setImageDrawable(ContextCompat.getDrawable(
                context,
                R.drawable.ic_rainy_selected))
            Weather.SNOW.name -> binding.dialogStampWeather.setImageDrawable(ContextCompat.getDrawable(
                context,
                R.drawable.ic_snowy_selected))
        }
    }

    private fun stamp() {
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            56f,
            context.resources.displayMetrics)
        for (i in 0..diary.stampList.size - 1) {
            val haveToStamp = diary.stampList.get(i)

            var stamp = View.inflate(context, R.layout.stamp, null)
            for (i in 0..list.size - 1) {
                if (list.get(i).stampTag == haveToStamp.stampType) {
                    stamp.setBackgroundResource(list.get(i).stamp96ID)
                    break
                }
            }


            stamp.x =
                (haveToStamp.x * binding.dialogStampController.width).toFloat()
            stamp.y =
                (haveToStamp.y * binding.dialogStampController.height).toFloat()

            if (stamp.x >= binding.dialogStampController.width - pixels) {
                stamp.x = binding.dialogStampController.width - pixels
            }
            if (stamp.y >= binding.dialogStampController.height - pixels) {
                stamp.y = binding.dialogStampController.height - pixels
            }

            binding.dialogStampController.addView(stamp)
            stamp.updateLayoutParams {

                height = pixels.toInt()
                width = pixels.toInt()


            }


        }
    }

    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB",
            R.drawable.ic_stamp_greatjob,
            R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT", R.drawable.ic_stamp_perfect, R.drawable.ic_stamp96_perfect))
        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED", R.drawable.ic_stamp_hundred, R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(StampData("INTERESTING",
            R.drawable.ic_stamp_interesting,
            R.drawable.ic_stamp96_interesting))
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
    }
}