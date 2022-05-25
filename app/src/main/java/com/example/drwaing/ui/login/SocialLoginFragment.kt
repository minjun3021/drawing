package com.example.drwaing.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.drwaing.Network
import com.example.drwaing.NetworkInterface
import com.example.drwaing.R
import com.example.drwaing.SuccessLottieFragment
import com.example.drwaing.databinding.FragmentSocialLoginBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.login.SocialLoginFragment.Companion.SOCIAL_TYPE_GOOGLE
import com.example.drwaing.ui.login.SocialLoginFragment.Companion.SOCIAL_TYPE_KAKAO
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Response


class SocialLoginFragment : Fragment(R.layout.fragment_social_login) {

    companion object {
        const val SOCIAL_TYPE_KAKAO = "KAKAO"
        const val SOCIAL_TYPE_GOOGLE = "GOOGLE"
    }

    private val binding: FragmentSocialLoginBinding by viewBinding(FragmentSocialLoginBinding::bind)

    private val navController: NavController
        get() = findNavController(
            requireActivity(),
            R.id.nav_login_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()


    }


    private fun initView() {
        binding.llKakaoLogin.setOnClickListener {
            loginKakao()
        }
        binding.llGoogleLogin.setOnClickListener {
            logingoogle()
        }
    }


    private fun loginKakao() {

        //카카오게정로그인 콜백
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {//실패

            } else if (token != null) {
                sign(SOCIAL_TYPE_KAKAO)
            }
        }


        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {//카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) { //로그인 실패

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(
                        requireContext(),
                        callback = callback
                    )
                } else if (token != null) {
                    Log.e("카카오로그인 성공", token.accessToken)
                    sign(SOCIAL_TYPE_KAKAO)
                }
            }
        } else {//카카오 계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }

    }

    private fun logingoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken() 서버의 클라이언트 아이디가 필요함 https://developers.google.com/identity/sign-in/android/backend-aut
                //TODO 나중에 하기
            .build()
        var mGoogleSignInClient =
            GoogleSignIn.getClient(requireContext(), gso)
        var signInIntent: Intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,333)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 333) {

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            Log.e("이름",account.idToken.toString())
        }
    }

    fun sign(type: String) {

        lateinit var signRequest: NetworkInterface.SignRequest
        when (type) {
            SOCIAL_TYPE_KAKAO -> {
                signRequest =
                    NetworkInterface.SignRequest(AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!.accessToken,
                        SOCIAL_TYPE_KAKAO)
            }

            SOCIAL_TYPE_GOOGLE -> {

            }
        }

        Network.api.signin(signRequest).enqueue(object :
            retrofit2.Callback<NetworkInterface.SignResponse> {
            override fun onResponse(
                call: Call<NetworkInterface.SignResponse>,
                response: Response<NetworkInterface.SignResponse>,
            ) {
                if (response.code() == 200) {
                    Log.e("kakaologin : ", response.code().toString() + response.message())
                    navController.navigate(
                        R.id.action_socialLoginFragment_to_successLottieFragment,
                        bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_LOGIN))


                } else if (response.code() == 401) { // Unauthorized
                    Network.api.signup(signRequest).enqueue(object :
                        retrofit2.Callback<NetworkInterface.SignResponse> {
                        override fun onResponse(
                            call: Call<NetworkInterface.SignResponse>,
                            response: Response<NetworkInterface.SignResponse>,
                        ) {
                            //회원가입성공
                            navController.navigate(
                                R.id.action_socialLoginFragment_to_successLottieFragment,
                                bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_LOGIN))
                        }

                        override fun onFailure(
                            call: Call<NetworkInterface.SignResponse>,
                            t: Throwable,
                        ) {
                            Log.e("signup", t.message.toString())
                        }
                    })
                } else {
                    Log.e("kakaologin : ", response.code().toString() + response.message())
                }
            }

            override fun onFailure(call: Call<NetworkInterface.SignResponse>, t: Throwable) {
                Log.e("signin", t.message.toString())
            }
        })

    }
}