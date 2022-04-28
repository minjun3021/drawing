package com.example.drwaing.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.viewBinding


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {

    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}