package com.example.drwaing.ui.stamp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.drwaing.R
import com.example.drwaing.data.diary.Stamped
import com.example.drwaing.data.diary.Weather
import com.example.drwaing.databinding.ActivityStampBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.diary.DiaryActivity
import com.example.drwaing.ui.main.MainFragment
import java.util.*
import kotlin.collections.ArrayList


class StampActivity : AppCompatActivity() {
    private val binding: ActivityStampBinding by viewBinding(ActivityStampBinding::inflate)
    private val stampAdatper: StampAdapter by lazy { StampAdapter() }
    private val viewModel: StampViewModel by viewModels()
    private var stampDiaryId =-1

    var list: ArrayList<StampData> = ArrayList()
    var stamps: ArrayList<Stamped> = ArrayList(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        observeViewing()
    }

    override fun onResume() {

        stampDiaryId=intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)!!
        viewModel.getDiary(stampDiaryId)
        super.onResume()
    }

    fun initView() {
        binding.activityStampRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = stampAdatper
        }

        submitStampData()


        binding.activityStampContent.post {

            binding.activityStampContent.background = DiaryActivity.createBitmap(this,
                binding.activityStampContent.width,
                binding.activityStampContent.lineHeight)
        }


        binding.activityStampFake.post {

            binding.activityStampFake.background = DiaryActivity.createBitmap(this,
                binding.activityStampContent.width,
                binding.activityStampContent.lineHeight)
        }
        binding.activityStampBack.setOnClickListener {
            saveStamps()
            finish()//TODO: 다이얼로그 + 도장 서버 저장 }
        }

        binding.activityStampController.setOnTouchListener { view, motionEvent ->

            val whichStamp = StampAdapter.lastClickedPosition


            when (motionEvent.getActionMasked()) {
                MotionEvent.ACTION_DOWN -> {
                    if (whichStamp > -1) {
                        var stamp = View.inflate(applicationContext, R.layout.stamp, null)
                        stamp.setBackgroundResource(list.get(whichStamp).stamp96ID)

                        stamp.x = (motionEvent.getX()) - (binding.stamp.width / 2)
                        stamp.y = (motionEvent.getY()) - (binding.stamp.height / 2)
                        //Log.e(stamp.x.toString(), stamp.y.toString())
                        if (isInsideDrawing(stamp.x.toDouble(),stamp.y.toDouble())) {
                            if (stamps.size != 0) {
                                for (i in 0..stamps.size - 1) {
                                    if (stamps.get(i).which == whichStamp) {
                                        binding.activityStampController.removeView(stamps.get(i).view)
                                        stamps.remove(stamps.get(i))
                                        break
                                    }
                                }
                            }


                            binding.activityStampController.addView(stamp)
                            stamps.add(Stamped(stamp, whichStamp))

                            stamp.updateLayoutParams {
                                val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    96f,
                                    applicationContext.resources.displayMetrics)
                                height = pixels.toInt()
                                width = pixels.toInt()


                            }
                        }
                    }


                    true
                }
                else -> false

            }
        }




    }
    fun saveStamps() {
        if (stamps.size != 0) {
            for (i in 0..stamps.size - 1) {

                val x : Double = (stamps.get(i).view.x/binding.activityStampController.width).toDouble()
                val y : Double = (stamps.get(i).view.y/binding.activityStampController.height).toDouble()
                val formatX= String.format(Locale.KOREAN,"%.3f",x).toDouble()
                val formatY=String.format(Locale.KOREAN,"%.3f",y).toDouble()
                viewModel.sendStamp(stampDiaryId,list.get(stamps.get(i).which).stampTag,formatX,formatY)
            }
        }
    }
    private fun observeViewing() {
        viewModel.diary.observe(this, Observer {
            binding.activityStampDate.text =
                MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.activityStampDrawing)

            binding.activityStampContent.text = viewModel.diary.value!!.content
            setIcon()
        })


    }
    fun submitStampData(){
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB", R.drawable.ic_stamp_greatjob, R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT", R.drawable.ic_stamp_perfect, R.drawable.ic_stamp96_perfect))
        //TODO 얘네들이 들어가면 오류뜸 왜그런진 모르겟음
//        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
//        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED", R.drawable.ic_stamp_hundred, R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(StampData("INTERESTING", R.drawable.ic_stamp_interesting, R.drawable.ic_stamp96_interesting))
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
        stampAdatper.submitList(list)
    }
    fun isInsideDrawing(x: Double, y: Double): Boolean {
        val allowedStampStartX = binding.activityStampBg.x
        val allowedStampEndX = binding.activityStampBg.x +binding.activityStampBg.width-binding.stamp.width

        val allowedStampStartY = binding.activityStampBg.y
        val allowedStampEndY = binding.activityStampBg.y+binding.activityStampBg.height-binding.stamp.height

        return (x in allowedStampStartX..allowedStampEndX && y in allowedStampStartY ..allowedStampEndY)

    }

    private fun setIcon() {
        when (viewModel.diary.value!!.weather) {
            Weather.SUN.name -> binding.activityStampSun.setImageDrawable(ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_sunny_selected))
            Weather.CLOUD.name -> binding.activityStampCloud.setImageDrawable(ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_cloudy_selected))
            Weather.RAIN.name -> binding.activityStampRain.setImageDrawable(ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_rainy_selected))
            Weather.SNOW.name -> binding.activityStampSnow.setImageDrawable(ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_snowy_selected))
        }
    }
    override fun onBackPressed() {
        saveStamps()
        finish()
        super.onBackPressed()
    }


}