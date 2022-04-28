package com.example.drwaing.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentTypingDiaryContentBinding
import com.example.drwaing.extension.viewBinding

class TypingDiaryContentFragment : Fragment(R.layout.fragment_typing_diary_content) {

    private val binding by viewBinding(FragmentTypingDiaryContentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}