package com.kmj.ssgssg.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.FragmentNoticeBinding
import com.kmj.ssgssg.extension.viewBinding


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