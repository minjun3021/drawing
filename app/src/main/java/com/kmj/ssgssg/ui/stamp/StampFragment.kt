package com.kmj.ssgssg.ui.stamp

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kmj.ssgssg.R
import com.kmj.ssgssg.SuccessLottieFragment
import com.kmj.ssgssg.base.LoadingDialog
import com.kmj.ssgssg.data.diary.Weather

import com.kmj.ssgssg.databinding.FragmentStampBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.diary.DiaryActivity
import com.kmj.ssgssg.ui.main.MainFragment
import java.util.*
import kotlin.collections.ArrayList


class StampFragment : Fragment(R.layout.fragment_stamp) {

    private val binding by viewBinding(FragmentStampBinding::bind)
    private val stampAdatper: StampAdapter by lazy { StampAdapter(::makeNewStamp, ::removeStamp) }
    private val viewModel: StampViewModel by activityViewModels()
    lateinit var loadingDialog: LoadingDialog

    var list: ArrayList<StampData> = ArrayList()
    var currentSelectedStampPosition: Int? = null

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_stamp_host
        )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRandomDiary()
        initView()
        observeViewing()

    }


    fun initView() {

        binding.close.setOnClickListener {
            stampAdatper.currentSelectedPosition=-1
             stampAdatper.notifyItemChanged(currentSelectedStampPosition!!)
            currentSelectedStampPosition=-1
            binding.fragmentStampMover.requestDisallowInterceptTouchEvent(true)
            setVisibility(1)
        }

        binding.fragmentStampBack.setOnClickListener {
            activity?.finish()

        }
        binding.fragmentStampRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = stampAdatper
        }
        submitStampData()
        binding.fragmentStampDate.setTypeface(MainFragment.typeface)
        binding.fragmentStampContent.setTypeface(MainFragment.typeface)
        binding.fragmentStampContent.post {
            binding.fragmentStampContent.background = DiaryActivity.createBitmap(
                requireContext(),
                binding.fragmentStampContent.width,
                binding.fragmentStampContent.lineHeight
            )
        }

        binding.fragmentStampFake.post {

            binding.fragmentStampFake.background = DiaryActivity.createBitmap(
                requireContext(),
                binding.fragmentStampContent.width,
                binding.fragmentStampContent.lineHeight
            )
        }


        binding.fragmentStampComplete.setOnTouchListener { v, event ->
            when (event.action) {

                MotionEvent.ACTION_UP -> {
                    binding.fragmentStampComplete.setColorFilter(null)

                }
                MotionEvent.ACTION_DOWN -> {
                    binding.fragmentStampComplete.setColorFilter(
                        Color.argb(40, 255, 255, 255),
                        PorterDuff.Mode.SRC_ATOP
                    )

                }

            }
            false

        }
        binding.fragmentStampComplete.setOnClickListener {

            loadingDialog = LoadingDialog(requireContext()).apply { show() }
            stampAdatper.isClickable = false
            binding.fragmentStampMover.setOnTouchListener(null)
            setVisibility(2)

            val x: Double =
                (binding.fragmentStampMover.x / binding.fragmentStampController.width).toDouble()
            val y: Double =
                (binding.fragmentStampMover.y / binding.fragmentStampController.height).toDouble()
            val formatX = String.format(Locale.KOREAN, "%.3f", x).toDouble()
            val formatY = String.format(Locale.KOREAN, "%.3f", y).toDouble()
            viewModel.sendStamp(
                viewModel.diary.value!!.diaryId,
                list[currentSelectedStampPosition!!].stampTag,
                formatX,
                formatY
            )
        }

    }

    private fun setVisibility(case: Int) {
        binding.apply {
            when (case) {
                0 -> {
                    stamp.visibility = View.VISIBLE
                    close.visibility = View.VISIBLE
                    fragmentStampCompletetext.visibility = View.VISIBLE
                    fragmentStampComplete.visibility = View.VISIBLE
                    //
                }
                1 -> {
                    stamp.visibility = View.INVISIBLE
                    close.visibility = View.INVISIBLE
                    fragmentStampCompletetext.visibility = View.INVISIBLE
                    fragmentStampComplete.visibility = View.INVISIBLE

                }
                2 -> {

                    stamp.visibility = View.VISIBLE
                    close.visibility = View.INVISIBLE
                    fragmentStampCompletetext.visibility = View.INVISIBLE
                    fragmentStampComplete.visibility = View.INVISIBLE
                }

            }
        }

    }

    private fun removeStamp(position: Int) {}

    private fun makeNewStamp(position: Int) {

        currentSelectedStampPosition?.let {
            stampAdatper.notifyItemChanged(it)
        }
        currentSelectedStampPosition = position
        stampAdatper.notifyItemChanged(currentSelectedStampPosition!!)
        binding.fragmentStampMover.setOnTouchListener { v, event ->

            binding.fragmentStampScroll.requestDisallowInterceptTouchEvent(true) //parent뷰의 이벤트 강탈 방지


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
                    else if (v.x + v.width > binding.fragmentStampController.width) v.x =
                        (binding.fragmentStampController.width - v.width).toFloat()

                    if (v.y < 0) v.y = 0F
                    else if (v.y + v.height > binding.fragmentStampController.height) v.y =
                        (binding.fragmentStampController.height - v.height).toFloat()

                }
            }
            true
        }

        
        binding.fragmentStampMover.apply {
            bringToFront()
            x =
                (((binding.fragmentStampController.width) - (binding.fragmentStampMover.width)) / 2).toFloat()
            y =
                (((binding.fragmentStampController.height) - (binding.fragmentStampMover.height)) / 2).toFloat()
        }
        binding.stamp.setImageResource(list[position].stamp96ID)
        setVisibility(0)

    }

    private fun observeViewing() {
        viewModel.diary.observe(viewLifecycleOwner) {
            binding.fragmentStampDate.text =
                MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentStampDrawing)

            binding.fragmentStampContent.text = viewModel.diary.value!!.content
            setIcon()

            if (viewModel.diary.value?.stampList?.size != 0) {
                val pixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    96f,
                    this.resources.displayMetrics
                )
                for (i in 0..viewModel.diary.value?.stampList!!.size - 1) {
                    val haveToStamp = viewModel.diary.value!!.stampList.get(i)
                    var stamp = View.inflate(requireContext(), R.layout.stamp, null)
                    for (i in 0..list.size - 1) {
                        if (list.get(i).stampTag == haveToStamp.stampType) {
                            stamp.setBackgroundResource(list.get(i).stamp96ID)
                            break
                        }
                    }
                    stamp.x = (haveToStamp.x * binding.fragmentStampController.width).toFloat()
                    stamp.y = (haveToStamp.y * binding.fragmentStampController.height).toFloat()
                    if (stamp.x >= binding.fragmentStampController.width - pixels) {
                        stamp.x = binding.fragmentStampController.width - pixels
                    }
                    if (stamp.y >= binding.fragmentStampController.height - pixels) {
                        stamp.y = binding.fragmentStampController.height - pixels
                    }
                    binding.fragmentStampController.addView(stamp)
                    stamp.updateLayoutParams {
                        height = pixels.toInt()
                        width = pixels.toInt()
                    }
                }
            }
        }

        viewModel.timeToNavigate.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog.cancel()
                navController.navigate(R.id.action_stampFragment_to_successLottieFragment3,
                    bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_STAMP)
                )
            }
        }
    }

    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(
            StampData(
                "GREATJOB",
                R.drawable.ic_stamp_greatjob,
                R.drawable.ic_stamp96_greatjob
            )
        )
        list.add(
            StampData(
                "PERFECT",
                R.drawable.ic_stamp_perfect,
                R.drawable.ic_stamp96_perfect
            )
        )
        list.add(StampData("OH", R.drawable.ic_stamp_oh, R.drawable.ic_stamp96_oh))
        list.add(StampData("ZZUGUL", R.drawable.ic_stamp_zzugul, R.drawable.ic_stamp96_zzugul))
        list.add(
            StampData(
                "HUNDRED",
                R.drawable.ic_stamp_hundred,
                R.drawable.ic_stamp96_hundred
            )
        )
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(
            StampData(
                "INTERESTING",
                R.drawable.ic_stamp_interesting,
                R.drawable.ic_stamp96_interesting
            )
        )
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
        stampAdatper.submitList(list)
    }


    private fun setIcon() {
        when (viewModel.diary.value!!.weather) {
            Weather.SUN.name -> binding.fragmentStampSun.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_sunny_selected
                )
            )
            Weather.CLOUD.name -> binding.fragmentStampCloud.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_cloudy_selected
                )
            )
            Weather.RAIN.name -> binding.fragmentStampRain.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_rainy_selected
                )
            )
            Weather.SNOW.name -> binding.fragmentStampSnow.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_snowy_selected
                )
            )
        }
    }


}
