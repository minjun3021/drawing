package com.example.drwaing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.drwaing.databinding.FragmentMainBinding
import com.example.drwaing.extension.viewBinding


class MainFragment : Fragment() {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}