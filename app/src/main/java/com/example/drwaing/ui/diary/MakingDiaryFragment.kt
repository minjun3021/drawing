package com.example.drwaing.ui.diary

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.drwaing.R
import com.example.drwaing.SuccessLottieFragment
import com.example.drwaing.data.diary.Stamped
import com.example.drwaing.data.diary.Weather
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.showDialog
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainFragment
import com.example.drwaing.ui.stamp.StampActivity
import com.example.drwaing.ui.stamp.StampData
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {
    private val clicked: Boolean = false
    private lateinit var callback: OnBackPressedCallback
    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)
    private val viewModel: DrawingViewModel by activityViewModels()
    private var readyToUploadDiary =false

    var list: ArrayList<StampData> = ArrayList()

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()


    }

    private fun initView() {


        binding.fragmentMakingDate.setTypeface(MainFragment.typeface)
        binding.fragmentMakingContent.setTypeface(MainFragment.typeface)
        binding.fragmentMakingDate.setText(makeDate())


        when (activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_VIEW_TYPE)) {
            0 -> {//new

                val now = System.currentTimeMillis()
                val date = Date(now)
                val sdf = SimpleDateFormat("yyyy-MM-dd");
                val getTime: String = sdf.format(date) + "T"
                binding.fragmentMakingDate.text = MainFragment.makeDirayDate(getTime)

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
                binding.fragmentMakingContent.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingFake.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingOkay.setOnClickListener {

                    if(readyToUploadDiary){

                        val file = bitmapToFile(viewModel.bitmap.value)
                        if (file != null) {
                            viewModel.save(file)

                        }
                        navController.navigate(
                            R.id.action_makingDiaryFragment_to_successLottieFragment,
                            bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
                        )
                    }
                }
                binding.fragmentMakingSun.setOnClickListener {
                    viewModel.setWeather(Weather.SUN)
                }
                binding.fragmentMakingCloud.setOnClickListener {
                    viewModel.setWeather(Weather.CLOUD)
                }
                binding.fragmentMakingRain.setOnClickListener {
                    viewModel.setWeather(Weather.RAIN)
                }

                binding.fragmentMakingSnow.setOnClickListener {
                    viewModel.setWeather(Weather.SNOW)
                }
                callback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        context?.showDialog {
                            title = "열심히 적은 일기장을 찢으시겠어요?"
                            positiveText = "네"
                            negativeText = "아니오"
                            onPositiveClickListener = {
                                activity?.finish()
                            }
                            onNegativeClickListener = {
                                dismiss()
                            }

                        }
                    }
                }
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
                observeMaking()
            }
            1 -> {//edit 나중에 추가

            }
            2 -> {//view

                binding.fragmentMakingOkay.visibility = View.GONE
                binding.fragmentMakingInstagram.visibility = View.VISIBLE
                binding.fragmentMakingInstagram.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_shareDiaryFragment)
                }

                Log.e("diaryid",
                    activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY).toString())
                submitStampData()
                observeViewing()
                viewModel.getDiary(activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)!!)



            }
        }
        binding.fragmentMakingBack.setOnClickListener {
            context?.showDialog {
                title = "열심히 적은 일기장을 찢으시겠어요?"
                positiveText = "네"
                negativeText = "아니오"
                onPositiveClickListener = {
                    activity?.finish()
                }
                onNegativeClickListener = {
                    dismiss()
                }

            }
        }



        binding.fragmentMakingFake.post {
            binding.fragmentMakingFake.background = DiaryActivity.createBitmap(requireContext(),
                binding.fragmentMakingContent.width,
                binding.fragmentMakingContent.lineHeight)
        }
        binding.fragmentMakingContent.post {
            binding.fragmentMakingContent.background = DiaryActivity.createBitmap(requireContext(),
                binding.fragmentMakingContent.width,
                binding.fragmentMakingContent.lineHeight)
        }

    }

    private fun observeMaking() {
        viewModel.diaryText.observe(viewLifecycleOwner) {
            binding.fragmentMakingContent.text = it.replace(" ", "\u00A0")
            checkDoEveryThing()
        }

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.fragmentMakingDrawing.setImageBitmap(it)

        }
        viewModel.weather.observe(viewLifecycleOwner) {
            changeIcon()

        }


    }

    private fun observeViewing() {
        viewModel.diary.observe(viewLifecycleOwner) {
            binding.fragmentMakingDate.text =
                MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentMakingDrawing)

            binding.fragmentMakingContent.text = viewModel.diary.value!!.content
            setIcon()

            if (viewModel.diary.value?.stampList?.size != 0) {
                val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    96f,
                    requireContext().resources.displayMetrics)

                for (i in 0..viewModel.diary.value?.stampList!!.size - 1) {

                    val haveToStamp=viewModel.diary.value!!.stampList.get(i)
                    var stamp = View.inflate(context, R.layout.stamp, null)
                    for (i in 0..list.size - 1) {
                        if (list.get(i).stampTag == haveToStamp.stampType) {
                            stamp.setBackgroundResource(list.get(i).stamp96ID)
                            break
                        }
                    }
                    stamp.x = (haveToStamp.x*binding.fragmentMainStampController.width).toFloat()
                    stamp.y = (haveToStamp.y*binding.fragmentMainStampController.height).toFloat()
                    if(stamp.x>= binding.fragmentMainStampController.width-pixels){
                        stamp.x=binding.fragmentMainStampController.width-pixels
                    }
                    if(stamp.y>= binding.fragmentMainStampController.height-pixels){
                        stamp.y=binding.fragmentMainStampController.height-pixels
                    }
                    Log.e(stamp.x.toString(),stamp.y.toString())

                    binding.fragmentMainStampController.addView(stamp)
                    stamp.updateLayoutParams {

                        height = pixels.toInt()
                        width = pixels.toInt()


                    }


                }
            }
        }



    }
    private fun checkDoEveryThing(){
        if(binding.fragmentMakingContent.text.isNotEmpty().and(viewModel.weather.value!=null)){
            binding.fragmentMakingOkay.setTextColor(Color.parseColor("#111111"))
            readyToUploadDiary=true
        }
    }
    private fun changeIcon() {
        checkDoEveryThing()
        binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_sunny_enabled))
        binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_cloudy_enabled))
        binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_rainy_enabled))
        binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_snowy_enabled))

        when (viewModel.weather.value!!.name) {
            Weather.SUN.name -> binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_sunny_selected))
            Weather.CLOUD.name -> binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_cloudy_selected))
            Weather.RAIN.name -> binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_rainy_selected))
            Weather.SNOW.name -> binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_snowy_selected))
        }
    }

    private fun setIcon() {
        when (viewModel.diary.value!!.weather) {
            Weather.SUN.name -> binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_sunny_selected))
            Weather.CLOUD.name -> binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_cloudy_selected))
            Weather.RAIN.name -> binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_rainy_selected))
            Weather.SNOW.name -> binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_snowy_selected))
        }
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
    }
    private fun makeDate(): String {
        val date = Date()
        val mFormat = SimpleDateFormat("yyyy.MM.dd")
        var time: String = mFormat.format(date)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeekNumber = calendar[Calendar.DAY_OF_WEEK]
        when(dayOfWeekNumber){
            1-> time += " 일요일"
            2-> time += " 월요일"
            3-> time += " 화요일"
            4-> time += " 수요일"
            5-> time += " 목요일"
            6-> time += " 금요일"
            7-> time += " 토요일"
        }
        return time
    }

    private fun bitmapToFile(bitmap: Bitmap?): File? {
        bitmap ?: return null
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("image", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    override fun onDetach() {
        super.onDetach()
        if(::callback.isInitialized)
            callback.remove()
    }
}