package com.example.drwaing.ui.diary

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
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
        binding.fragmentMakingBack.setOnClickListener {
            //TODO dialog 뛰우기
            navController.popBackStack()
        }

        binding.fragmentMakingOkay.setOnClickListener {
            navController.navigate(
                R.id.action_makingDiaryFragment_to_successLottieFragment,
                bundleOf("WhereIFrom" to "Making")
            )
        }

        binding.fragmentMakingDrawing.setOnClickListener {
            navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
        }

        binding.fragmentMakingContent.setOnClickListener {
            navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
        }
    }

    private fun observe() {
        viewModel.diaryText.observe(viewLifecycleOwner) {
            binding.fragmentMakingContent.text = it.replace(" ", "\u00A0")
        }
    }
}