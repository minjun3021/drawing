package com.example.drwaing

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.drwaing.databinding.FragmentSocialLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class SocialLoginFragment : Fragment() {

    private var mBinding: FragmentSocialLoginBinding? = null
    private val binding get() = mBinding!!
    private var mContainer: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mContainer = container!!
        mBinding = FragmentSocialLoginBinding.inflate(inflater, container, false)

        binding.llKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(container!!.context)) {
                UserApiClient.instance.loginWithKakaoTalk(container!!.context) { token, error ->
                    if (error != null) {
                        Log.e("TAG", "카카오톡으로 로그인 실패", error)


                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        UserApiClient.instance.loginWithKakaoAccount(
                            container!!.context,
                            callback = callback
                        )
                    } else if (token != null) {
                        Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        container.findNavController()
                            .navigate(R.id.action_socialLoginFragment_to_loginSuccessFragment2)

                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    container!!.context,
                    callback = callback
                )
            }


        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("TAG", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.e("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            mContainer!!.findNavController()
                .navigate(R.id.action_socialLoginFragment_to_loginSuccessFragment2)

        }
    }
}