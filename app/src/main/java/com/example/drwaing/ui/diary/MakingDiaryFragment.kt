package com.example.drwaing.ui.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.viewBinding


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {

    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.fragmentMakingBack.setOnClickListener {
            //TODO dialog 뛰우기
            navController.popBackStack()
        }

        binding.fragmentMakingOkay.setOnClickListener {
            navController.navigate(R.id.action_makingDiaryFragment_to_successLottieFragment,
                bundleOf("WhereIFrom" to "Making"))
        }

        binding.fragmentMakingDrawing.setOnClickListener {
            navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
        }

        //자동줄바꿈 방지 코드
        var myString:String =binding.fragmentMakingContent.text.toString().replace(" ", "\u00A0");
        binding.fragmentMakingContent.text=myString

        binding.fragmentMakingContent.setOnClickListener {
            navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
        }
    }
}