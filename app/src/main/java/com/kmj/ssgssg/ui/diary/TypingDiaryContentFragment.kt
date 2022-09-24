package com.kmj.ssgssg.ui.diary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.FragmentTypingDiaryContentBinding
import com.kmj.ssgssg.extension.showDialog
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.main.MainFragment


class TypingDiaryContentFragment : Fragment(R.layout.fragment_typing_diary_content) {

    private lateinit var callback: OnBackPressedCallback
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

        binding.etDiaryContent.setTypeface(MainFragment.typeface)


        binding.etDiaryContent.setText(viewModel.diaryText.value ?: "")
        binding.fragmentContentBack.setOnClickListener {
            context?.showDialog {
                title = "변경사항을 저장하지 않으시겠어요?"
                positiveText = "네"
                negativeText = "취소"
                onPositiveClickListener = {
                    navController.popBackStack()
                }
                onNegativeClickListener = {
                    dismiss()
                }
            }

        }

        binding.fragmentContentOkay.setOnClickListener {
            viewModel.diaryText.value = binding.etDiaryContent.text?.toString() ?: ""
            navController.popBackStack()
        }

        binding.etDiaryContent.post {
            binding.etDiaryContent.background = DiaryActivity.createBitmap(requireContext(),
                binding.etDiaryContent.width,
                binding.etDiaryContent.lineHeight)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                context?.showDialog {
                    title = "변경사항을 저장하지 않으시겠어요?"
                    positiveText = "네"
                    negativeText = "취소"
                    onPositiveClickListener = {

                        navController.popBackStack()
                    }
                    onNegativeClickListener = {
                        dismiss()
                    }

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


}