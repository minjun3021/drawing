package com.example.drwaing.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentTypingDiaryContentBinding
import com.example.drwaing.extension.viewBinding

class TypingDiaryContentFragment : Fragment(R.layout.fragment_typing_diary_content) {

    private val binding by viewBinding(FragmentTypingDiaryContentBinding::bind)

    private val navController : NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentContentBack.setOnClickListener {
            //TODO dialog
            navController.popBackStack()

        }

        binding.fragmentContentOkay.setOnClickListener {
            //TODO bundle
            navController.popBackStack()
        }
    }
}