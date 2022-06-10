package com.example.drwaing.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivitySettingBinding
import com.example.drwaing.databinding.FragmentSettingBinding
import com.example.drwaing.extension.showDialog
import com.example.drwaing.extension.viewBinding
import okhttp3.internal.notify


class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val binding: FragmentSettingBinding by viewBinding(FragmentSettingBinding::bind)

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_setting_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    } 

    private fun initView() {
        binding.fragmentSettingBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.fragmentSettingFontsetting.setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_setFontFragment)

        }
        binding.fragmentSettingNotice.setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_noticeFragment)
        }
        binding.fragmentSettingAsk.setOnClickListener {
            Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.fragmentSettingLogout.setOnClickListener {
            context?.showDialog {
                title = "로그아웃 하시겠어요?"
                positiveText = "네"
                negativeText = "아니오"
                onPositiveClickListener = {
                    //TODO retrofit
                }
                onNegativeClickListener = {
                    dismiss()
                }
            }

        }
        binding.fragmentSettingDeleteaccount.setOnClickListener {
            context?.showDialog {
                title = "정말 탈퇴 하시겠어요?"
                positiveText = "네"
                negativeText = "아니오"
                onPositiveClickListener = {
                    //TODO retrofit
                }
                onNegativeClickListener = {
                    dismiss()
                }
            }
        }
    }
}