package com.example.drwaing.ui.stamp

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivityStampBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.diary.DiaryActivity


class StampActivity : AppCompatActivity() {
    private val binding: ActivityStampBinding by viewBinding(ActivityStampBinding::inflate)
    private val stampAdatper: StampAdapter by lazy { StampAdapter() }
    private val viewModel : StampViewModel by viewModels()

    var list: ArrayList<StampData> = ArrayList()
    var stamps : ArrayList<Stamped> = ArrayList(12)
    var isStamped :Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }
    fun initView(){
        binding.activityStampRecycler.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=stampAdatper
        }

        list.add(StampData("GRAL",R.drawable.ic_stamp_gral,R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS",R.drawable.ic_stamp_dothis,R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD",R.drawable.ic_stamp_good,R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB",R.drawable.ic_stamp_greatjob,R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT",R.drawable.ic_stamp_perfect,R.drawable.ic_stamp96_perfect))
        //TODO 얘네들이 들어가면 오류뜸 왜그런진 모르겟음
//        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
//        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED",R.drawable.ic_stamp_hundred,R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG",R.drawable.ic_stamp_hoeng,R.drawable.ic_stamp96_hoeng))
        list.add(StampData("INTERESTING",R.drawable.ic_stamp_interesting,R.drawable.ic_stamp96_interesting))
        list.add(StampData("LOL",R.drawable.ic_stamp_lol,R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG",R.drawable.ic_stamp_zzang,R.drawable.ic_stamp96_zzang))
        stampAdatper.submitList(list)

        binding.activityStampContent.post {

            binding.activityStampContent.background = DiaryActivity.createBitmap(this, binding.activityStampContent.width, binding.activityStampContent.lineHeight)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val whichStamp =StampAdapter.lastClickedPosition

        if(whichStamp>-1) {
            when (event!!.getActionMasked()) {
                MotionEvent.ACTION_DOWN -> {

                    var stamp = View.inflate(applicationContext, R.layout.stamp, null)
                    stamp.setBackgroundResource(list.get(whichStamp).stamp96ID)

                    stamp.x = (event.getX()) - (binding.stamp.getWidth() / 2)
                    stamp.y = (event.getY()) - (binding.stamp.getHeight())

                    if(isStamped){
                        for (i in 0..stamps.size-1) {
                            if(stamps.get(i).posiotion==whichStamp)
                            {
                                binding.activityStampControler.removeView(stamps.get(i).view)
                                stamps.remove(stamps.get(i))
                                break
                            }
                        }
                    }


                    binding.activityStampControler.addView(stamp)
                    stamps.add(Stamped(stamp,whichStamp))
                    isStamped=true


                    stamp.updateLayoutParams {
                        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            80f,
                            applicationContext.resources.displayMetrics)
                        height = pixels.toInt()
                        width = pixels.toInt()
                    }
                }

            }
        }


        return super.onTouchEvent(event)
    }
    data class Stamped(
        val view :View,
        val posiotion :Int
    )
}