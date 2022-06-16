package com.example.drwaing.ui.diary

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
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
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
                binding.fragmentMakingContent.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingOkay.setOnClickListener {

                    val file = bitmapToFile(viewModel.bitmap.value)
                    Log.e("asdf","asdf")
                    if (file != null) {
                        Log.e("asasdfsdfdf","asdf")
                        viewModel.save(file)

                    }
                    //viewModel.upload("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Golde33443.jpg/420px-Golde33443.jpg")

//                    navController.navigate(
//                        R.id.action_makingDiaryFragment_to_successLottieFragment,
//                        bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
//                    )
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
            binding.fragmentMakingDate.text=viewModel.diary.value!!.createdDate
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentMakingDrawing)

            binding.fragmentMakingContent.text="content가 없네"
        }

    }
    private fun changeIcon(){
        binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_sunny_enabled))
        binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_cloudy_enabled))
        binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_rainy_enabled))
        binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_snowy_enabled))

        when(viewModel.weather.value){
            Weather.SUN-> binding.fragmentMakingSun.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_sunny_selected))
            Weather.CLOUD-> binding.fragmentMakingCloud.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_cloudy_selected))
            Weather.RAIN-> binding.fragmentMakingRain.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_rainy_selected))
            Weather.SNOW-> binding.fragmentMakingSnow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_snowy_selected))
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