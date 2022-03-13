package com.example.drwaing.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentSocialLoginBinding
import com.example.drwaing.extension.viewBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class SocialLoginFragment : Fragment(R.layout.fragment_social_login) {

    private val binding: FragmentSocialLoginBinding by viewBinding(FragmentSocialLoginBinding::bind)
    private val navController: NavController get() = findNavController(requireActivity(), R.id.nav_login_host)

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("TAG", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.e("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            navController.navigate(R.id.action_socialLoginFragment_to_loginSuccessFragment2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.llKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Log.e("TAG", "카카오톡으로 로그인 실패", error)

                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = callback
                        )
                    } else if (token != null) {
                        Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        navController.navigate(R.id.action_socialLoginFragment_to_loginSuccessFragment2)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    requireContext(),
                    callback = callback
                )
            }
        }
    }
}