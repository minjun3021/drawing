package com.example.drwaing.ui.diary

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.drwaing.R
import com.example.drwaing.SuccessLottieFragment
import com.example.drwaing.data.diary.Weather
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainFragment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {

    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)
    private val viewModel: DrawingViewModel by activityViewModels()

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
        when (activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_VIEW_TYPE)) {
            0 -> {//new

                val now = System.currentTimeMillis()
                val date=Date(now)
                val sdf = SimpleDateFormat("yyyy-MM-dd");
                val getTime: String = sdf.format(date)+"T"
                binding.fragmentMakingDate.text=MainFragment.makeDirayDate(getTime)

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
                binding.fragmentMakingContent.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingOkay.setOnClickListener {
                        //TODO 파일업로드가 안되서 주석 처놓음 밑에 일기 업로드는 테스트를 위해 이미지 링크를 다른거로 임시로 박아놓음
//                    val file = bitmapToFile(viewModel.bitmap.value)
//                    Log.e("asdf","asdf")
//                    if (file != null) {
//                        Log.e("asasdfsdfdf","asdf")
//                        viewModel.save(file)
//
//                    }
                    if(viewModel.weather.value!=null){
                        viewModel.upload("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Golde33443.jpg/420px-Golde33443.jpg")

                        navController.navigate(
                            R.id.action_makingDiaryFragment_to_successLottieFragment,
                            bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
                        )
                    }else{
                        Toast.makeText(context,"날씨를 선택하세요", Toast.LENGTH_SHORT).show();
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
                observeMaking()
            }
            1 -> {//edit 나중에 추가

            }
            2 -> {//view

                binding.fragmentMakingOkay.visibility = View.GONE
                binding.fragmentMakingInstagram.visibility = View.VISIBLE

                Log.e("diaryid",activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY).toString())
                observeViewing()
                viewModel.getDiary(activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)!!)

            }
        }
        binding.fragmentMakingBack.setOnClickListener {
            //TODO dialog 뛰우기
            activity?.finish()
        }

        binding.fragmentMakingContent.post {
            binding.fragmentMakingContent.background = DiaryActivity.createBitmap(requireContext(), binding.fragmentMakingContent.width, binding.fragmentMakingContent.lineHeight)
        }

    }

    private fun observeMaking() {
        viewModel.diaryText.observe(viewLifecycleOwner) {
            binding.fragmentMakingContent.text = it.replace(" ", "\u00A0")
        }

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.fragmentMakingDrawing.setImageBitmap(it)

        }
        viewModel.weather.observe(viewLifecycleOwner){
            changeIcon()
        }


    }
    private fun observeViewing() {
        viewModel.diary.observe(viewLifecycleOwner){
            binding.fragmentMakingDate.text=MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentMakingDrawing)

            binding.fragmentMakingContent.text="content가 없네"
            setIcon()
        }
    }
    private fun changeIcon(){
        binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_sunny_enabled))
        binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_cloudy_enabled))
        binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_rainy_enabled))
        binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_snowy_enabled))

        when(viewModel.weather.value!!.name){
            Weather.SUN.name-> binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_sunny_selected))
            Weather.CLOUD.name-> binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_cloudy_selected))
            Weather.RAIN.name-> binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_rainy_selected))
            Weather.SNOW.name-> binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_snowy_selected))
        }
    }
    private fun setIcon(){
        when(viewModel.diary.value!!.weather){
            Weather.SUN.name-> binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_sunny_selected))
            Weather.CLOUD.name-> binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_cloudy_selected))
            Weather.RAIN.name-> binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_rainy_selected))
            Weather.SNOW.name-> binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_snowy_selected))
        }
    }

    private fun bitmapToFile(bitmap: Bitmap?): File? {
        bitmap ?: return null
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpeg")

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
}