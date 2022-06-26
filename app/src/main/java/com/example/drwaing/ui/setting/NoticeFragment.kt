package com.example.drwaing.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentNoticeBinding
import com.example.drwaing.databinding.FragmentSetFontBinding
import com.example.drwaing.extension.viewBinding


class NoticeFragment : Fragment(R.layout.fragment_notice) {
    private val binding by viewBinding(FragmentNoticeBinding::bind)

    private val navController : NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_setting_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }
    private fun initView(){
        binding.fragmentNoticeBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}