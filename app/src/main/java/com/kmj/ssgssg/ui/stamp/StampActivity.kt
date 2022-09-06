package com.kmj.ssgssg.ui.stamp

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kmj.ssgssg.R
import com.kmj.ssgssg.base.LoadingDialog
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.databinding.ActivityStampBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.diary.DiaryActivity
import com.kmj.ssgssg.ui.main.MainFragment
import java.util.*
import kotlin.collections.ArrayList


class StampActivity : AppCompatActivity() {
    private val binding: ActivityStampBinding by viewBinding(ActivityStampBinding::inflate)
    private val stampAdatper: StampAdapter by lazy { StampAdapter(::makeNewStamp,::removeStamp) }
    private val viewModel: StampViewModel by viewModels()
    lateinit  var loadingDialog : LoadingDialog

    var list: ArrayList<StampData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.getRandomDiary()
        initView()
        observeViewing()
    }

    private fun removeStamp(position: Int) {
        binding.activityStampController.apply {
            for (i in childCount - 1 downTo 2) {

                if (getChildAt(i).background.constantState.toString() == list[position].constantState) {
                    list[position].constantState = null
                    binding.activityStampController.removeViewAt(i)
                    break
                }


            }
        }
    }

    private fun makeNewStamp(position: Int) {

        binding.activityStampController.apply {
            if (childCount != 2) {
                for (i in childCount - 1 downTo 2) {

                    if (getChildAt(i).x == (((binding.activityStampController.width) - (binding.stamp.width)) / 2).toFloat()
                        && getChildAt(i).y == (((binding.activityStampController.height) - (binding.stamp.height)) / 2).toFloat()
                    ) {
                        for ((index, item) in list.withIndex()) {
                            if (getChildAt(i).background.constantState.toString() == item.constantState) {
                                list[index].constantState = null
                                binding.activityStampController.removeViewAt(i)
                                stampAdatper.notifyItemChanged(index,Unit)
                                break
                            }
                        }


                    }

                }
            }
        }


        var stamp = View.inflate(applicationContext, R.layout.stamp, null)

        stamp.setBackgroundResource(list[position].stamp96ID)
        list[position].constantState = stamp.background.constantState.toString()

        stamp.x = (((binding.activityStampController.width) - (binding.stamp.width)) / 2).toFloat()
        stamp.y =
            (((binding.activityStampController.height) - (binding.stamp.height)) / 2).toFloat()


        stamp.setOnTouchListener { v, event ->

            binding.activityStampScroll.requestDisallowInterceptTouchEvent(true) //parent뷰의 이벤트 강탈 방지


            when (event.action) {

                MotionEvent.ACTION_UP -> { //움직일때놨을때

                }
                MotionEvent.ACTION_DOWN -> {//움직일때
                    v.bringToFront()
                }

                MotionEvent.ACTION_MOVE -> { //끌고갈때


                    v.setX(v.getX() + (event.getX()) - (v.getWidth() / 2))
                    v.setY(v.getY() + (event.getY()) - (v.getHeight() / 2))

                    if (v.x < 0) v.x = 0F
                    else if (v.x + v.width > binding.activityStampController.width) v.x =
                        (binding.activityStampController.width - v.width).toFloat()

                    if (v.y < 0) v.y = 0F
                    else if (v.y + v.height > binding.activityStampController.height) v.y =
                        (binding.activityStampController.height - v.height).toFloat()

                }
            }
            true
        }


        binding.activityStampController.addView(stamp)

        stamp.updateLayoutParams {
            val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                96f,
                applicationContext.resources.displayMetrics)
            height = pixels.toInt()
            width = pixels.toInt()


        }
    }


    fun initView() {

        binding.activityStampRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = stampAdatper
        }

        submitStampData()

        binding.activityStampDate.setTypeface(MainFragment.typeface)
        binding.activityStampContent.setTypeface(MainFragment.typeface)

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


        }

    }

    fun saveStamps() {
        binding.activityStampController.apply {
            if (childCount > 2) {
                loadingDialog=LoadingDialog(context).apply { show() }
                for (i in 2 until childCount) {
                    getChildAt(i).apply {
                        for (stamp in list) {

                            if (background.constantState.toString() == stamp.constantState){

                                val x: Double =
                                    (this.x / binding.activityStampController.width).toDouble()
                                val y: Double =
                                    (this.y / binding.activityStampController.height).toDouble()
                                val formatX = String.format(Locale.KOREAN, "%.3f", x).toDouble()
                                val formatY = String.format(Locale.KOREAN, "%.3f", y).toDouble()
                                viewModel.sendStamp(viewModel.diary.value!!.diaryId,
                                    stamp.stampTag,
                                    formatX,
                                    formatY)

                                break
                            }

                        }
                    }

                }
            }
            else{
                finish()
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

        viewModel.sendCount.observe(this, Observer {
            if (viewModel.sendCount.value!=0 && viewModel.sendCount.value==binding.activityStampController.childCount -2 )
            {
                loadingDialog.cancel()
                finish()
            }
        })


    }

    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB",
            R.drawable.ic_stamp_greatjob,
            R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT",
            R.drawable.ic_stamp_perfect,
            R.drawable.ic_stamp96_perfect))
        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED",
            R.drawable.ic_stamp_hundred,
            R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(StampData("INTERESTING",
            R.drawable.ic_stamp_interesting,
            R.drawable.ic_stamp96_interesting))
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
        stampAdatper.submitList(list)
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

    }


}