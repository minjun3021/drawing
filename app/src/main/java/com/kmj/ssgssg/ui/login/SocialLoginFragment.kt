package com.kmj.ssgssg.ui.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.kmj.ssgssg.R
import com.kmj.ssgssg.Network
import com.kmj.ssgssg.NetworkInterface
import com.kmj.ssgssg.SuccessLottieFragment
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.main.MainFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kmj.ssgssg.databinding.FragmentSocialLoginBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException


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
                Log.e("카카오로그인 성공", token.accessToken)
                sign(SOCIAL_TYPE_KAKAO,"")
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
                    sign(SOCIAL_TYPE_KAKAO,"")
                }
            }
        } else {//카카오 계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }

    }

    private fun logingoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken("450506962971-4sr7qe7j36kihvmenkjgop1gnvhac405.apps.googleusercontent.com")
          // .requestIdToken("476109625656-38td29igomdvtahgfuqr3ccbpn6vcqrl.apps.googleusercontent.com")
            //.requestIdToken() 서버의 클라이언트 아이디가 필요함 https://developers.google.com/identity/sign-in/android/backend-aut
            .build()


        var mGoogleSignInClient =
            GoogleSignIn.getClient(requireContext(), gso)
        var signInIntent: Intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 333)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 333) {
            if(resultCode==RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)

                if (task != null) {
                    val account = task.getResult(ApiException::class.java)

                    Log.e("구글토큰", account.idToken.toString())
                   //sign(SOCIAL_TYPE_GOOGLE,account.idToken.toString())
                    viewLifecycleOwner.lifecycleScope.launch {

                        kotlin.runCatching {
                            Log.e("서버통신전",account.idToken.toString())
                            Network.api.signin(account.idToken.toString(),
                                SOCIAL_TYPE_GOOGLE)
                        }.onSuccess {
                            MainFragment.token=it.accessToken

                            var sharedPref : SharedPreferences=requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                            var editor : SharedPreferences.Editor=sharedPref.edit()
                            editor.putString("token",it.accessToken)
                            editor.commit()

                            navController.navigate(
                                R.id.action_socialLoginFragment_to_successLottieFragment,
                                bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_LOGIN)
                            )
                        }.onFailure {
                            Log.e("회원가입","시도")
                            if (it is HttpException) {
                                when (it.code()) {
                                    500 -> {
                                        kotlin.runCatching {
                                            Network.api.signup(account.idToken.toString(),SOCIAL_TYPE_GOOGLE)
                                        }.onSuccess {
                                            //회원가입성공
                                            MainFragment.token=it.accessToken

                                            var sharedPref : SharedPreferences=requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                                            var editor : SharedPreferences.Editor=sharedPref.edit()
                                            editor.putString("token",it.accessToken)
                                            editor.commit()

                                            navController.navigate(
                                                R.id.action_socialLoginFragment_to_successLottieFragment,
                                                bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_REGISTER)
                                            )
                                        }.onFailure {
                                            Log.e("signupfail", it.toString())
                                        }
                                    }
                                    500->{
                                        Log.e("code",it.code().toString()+it.response().toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    fun sign(type: String,token :String) {
        lateinit var signRequest: NetworkInterface.SignRequest
        when (type) {
            SOCIAL_TYPE_KAKAO -> {
                signRequest =
                    NetworkInterface.SignRequest(
                        AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!.accessToken,
                        SOCIAL_TYPE_KAKAO
                    )
                viewLifecycleOwner.lifecycleScope.launch {

                    kotlin.runCatching {
                        Log.e(signRequest.socialType,signRequest.socialToken)
                        Network.api.signin(signRequest.socialToken,signRequest.socialType)
                    }.onSuccess {
                        MainFragment.token=it.accessToken

                        var sharedPref : SharedPreferences=requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                        var editor : SharedPreferences.Editor=sharedPref.edit()
                        editor.putString("token",it.accessToken)
                        editor.commit()

                        navController.navigate(
                            R.id.action_socialLoginFragment_to_successLottieFragment,
                            bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_LOGIN)
                        )
                    }.onFailure {

                        if (it is HttpException) {
                            when (it.code()) {
                                500 -> {
                                    kotlin.runCatching {
                                        Network.api.signup(signRequest.socialToken,signRequest.socialType)
                                    }.onSuccess {
                                        //회원가입성공
                                        MainFragment.token=it.accessToken

                                        var sharedPref : SharedPreferences=requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                                        var editor : SharedPreferences.Editor=sharedPref.edit()
                                        editor.putString("token",it.accessToken)
                                        editor.commit()

                                        navController.navigate(
                                            R.id.action_socialLoginFragment_to_successLottieFragment,
                                            bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_REGISTER)
                                        )
                                    }.onFailure {
                                        Log.e("signupfail", it.toString())
                                    }
                                }
                                500->{
                                    Log.e("code",it.code().toString()+it.response().toString())
                                }
                            }
                        }
                    }
                }
            }

            SOCIAL_TYPE_GOOGLE -> {
                signRequest =
                    NetworkInterface.SignRequest(

                        SOCIAL_TYPE_GOOGLE,token
                    )



            }
        }


    }
}