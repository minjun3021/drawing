package com.example.drwaing.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentShareDiaryBinding
import com.example.drwaing.databinding.FragmentSuccessLottieBinding
import com.example.drwaing.extension.viewBinding

class ShareDiaryFragment : Fragment() {
    private val binding by viewBinding(FragmentShareDiaryBinding::bind)
    private val navController : NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}