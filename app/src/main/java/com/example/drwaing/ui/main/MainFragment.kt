package com.example.drwaing.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentMainBinding
import com.example.drwaing.extension.viewBinding


class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}