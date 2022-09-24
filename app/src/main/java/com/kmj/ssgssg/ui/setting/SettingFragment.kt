package com.kmj.ssgssg.ui.setting

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kmj.ssgssg.Network
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.FragmentSettingBinding
import com.kmj.ssgssg.extension.showDialog
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.main.MainFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch


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


            //navController.navigate(R.id.action_settingFragment_to_noticeFragment)
        }
        binding.fragmentSettingAsk.setOnClickListener {


        }

        binding.fragmentSettingLogout.setOnClickListener {
            context?.showDialog {
                title = "로그아웃 하시겠어요?"
                positiveText = "네"
                negativeText = "아니오"
                onPositiveClickListener = {
                    logout()

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
                    viewLifecycleOwner.lifecycleScope.launch {
                        kotlin.runCatching {
                            Network.api.leave(MainFragment.token)
                        }.onSuccess {

                        }.onFailure {
                        }
                    }


                    logout()
                }
                onNegativeClickListener = {
                    dismiss()
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
            } else {
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