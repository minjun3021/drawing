package com.example.drwaing.ui.setting

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.Network
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivitySettingBinding
import com.example.drwaing.databinding.FragmentSettingBinding
import com.example.drwaing.extension.showDialog
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.launch
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

                FancyToast.makeText(requireContext(),
                    "서비스 준비중입니다"
                    , FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show()

            //navController.navigate(R.id.action_settingFragment_to_noticeFragment)
        }
        binding.fragmentSettingAsk.setOnClickListener {
            FancyToast.makeText(requireContext(),
                "서비스 준비중입니다"
                , FancyToast.LENGTH_SHORT, FancyToast.INFO,false).show()

        }

        binding.fragmentSettingLogout.setOnClickListener {
            context?.showDialog {
                title = "로그아웃 하시겠어요?"
                positiveText = "네"
                negativeText = "아니오"
                onPositiveClickListener = {
                    logout()
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
                        viewLifecycleOwner.lifecycleScope.launch {
                            kotlin.runCatching {
                                Network.api.leave(MainFragment.token)
                            }.onSuccess {

                            }.onFailure {

                            }
                        }


                        logout()
                        //TODO retrofit
                    }
                    onNegativeClickListener = {
                        dismiss()
                    }
                }
            }
        }
    }

    fun logout() {
        MainFragment.needToLogout=true
        MainFragment.token=""
        var sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit()

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("E", "연결 끊기 실패", error)
            } else {
                Log.e("E", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut()

        activity?.setResult(RESULT_OK);
        activity?.finish()

    }
}