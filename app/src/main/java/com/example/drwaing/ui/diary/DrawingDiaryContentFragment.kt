package com.example.drwaing.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentDrawingDiaryContentBinding
import com.example.drwaing.extension.viewBinding

class DrawingDiaryContentFragment : Fragment(R.layout.fragment_drawing_diary_content) {

    private val binding by viewBinding(FragmentDrawingDiaryContentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}