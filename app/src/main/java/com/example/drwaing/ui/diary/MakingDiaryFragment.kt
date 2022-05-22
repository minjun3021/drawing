package com.example.drwaing.ui.diary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.SuccessLottieFragment
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.viewBinding


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
        observe()
    }

    private fun initView() {
        when(activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_VIEW_TYPE)){
            0->{//new

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
                binding.fragmentMakingContent.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingOkay.setOnClickListener {
                    navController.navigate(
                        R.id.action_makingDiaryFragment_to_successLottieFragment,
                        bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
                    )
                }
            }
            1->{//edit 나중에 추가

            }
            2-> {//view
                binding.fragmentMakingOkay.visibility=View.GONE
                binding.fragmentMakingInstagram.visibility=View.VISIBLE

                activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)

            }
        }
        binding.fragmentMakingBack.setOnClickListener {
            //TODO dialog 뛰우기
            activity?.finish()
        }

        binding.fragmentMakingContent.post{
            binding.fragmentMakingContent.background=DiaryActivity.createBitmap(requireContext(), binding.fragmentMakingContent.width, binding.fragmentMakingContent.lineHeight)
        }

    }

    private fun observe() {
        viewModel.diaryText.observe(viewLifecycleOwner) {
            binding.fragmentMakingContent.text = it.replace(" ", "\u00A0")
        }
    }
}