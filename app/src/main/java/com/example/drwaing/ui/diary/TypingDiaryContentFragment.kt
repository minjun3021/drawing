package com.example.drwaing.ui.diary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentTypingDiaryContentBinding
import com.example.drwaing.extension.viewBinding


// TODO : 타이핑 줄 그려지는거 작업 되서 그냥 없애고 일기화면에서 해도 될듯?
class TypingDiaryContentFragment : Fragment(R.layout.fragment_typing_diary_content) {

    private val binding by viewBinding(FragmentTypingDiaryContentBinding::bind)
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
        binding.etDiaryContent.setText(viewModel.diaryText.value ?: "")
        binding.fragmentContentBack.setOnClickListener {
            //TODO dialog
            navController.popBackStack()

        }

        binding.fragmentContentOkay.setOnClickListener {
            viewModel.diaryText.value = binding.etDiaryContent.text?.toString() ?: ""
            //TODO bundle
            navController.popBackStack()
        }

        binding.etDiaryContent.post {
            binding.etDiaryContent.background = DiaryActivity.createBitmap(requireContext(), binding.etDiaryContent.width, binding.etDiaryContent.lineHeight)
        }
    }




}