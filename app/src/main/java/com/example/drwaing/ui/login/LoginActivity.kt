package com.example.drwaing.ui.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.Network
import com.example.drwaing.NetworkInterface
import com.example.drwaing.databinding.ActivityLoginBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBinding(ActivityLoginBinding::inflate)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //kakaoAutoLogin()
        googleAutoLogin()


    }



//    private fun kakaoAutoLogin() {
//        if (AuthApiClient.instance.hasToken()) {
//            UserApiClient.instance.accessTokenInfo { _, error ->
//                if (error != null) {
//                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
//
//                    } else {
//                        //기타 에러
//                    }
//                } else { //토큰이 있는 경우
//                    Log.e("kakaotokenexisted",AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!.accessToken)
//                    val signRequest =
//                        NetworkInterface.SignRequest(AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!.accessToken
//                            ,SocialLoginFragment.SOCIAL_TYPE_KAKAO)
//                    Network.api.signin(signRequest).enqueue(object : Callback<NetworkInterface.SignResponse>{
//                        override fun onResponse(
//                            call: Call<NetworkInterface.SignResponse>,
//                            response: Response<NetworkInterface.SignResponse>,
//                        ) {
//                            if (response.code()==200){
//                                Log.e("signin auto kakao",response.body()!!.accessToken)
//
//                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                                startActivity(intent)
//                                finish()
//                            }
//                            else{
//                                Log.e("signinautokakao",response.code().toString())
//                            }
//
//                        }
//
//                        override fun onFailure(
//                            call: Call<NetworkInterface.SignResponse>,
//                            t: Throwable,
//                        ) {
//                            Log.e("signin auto error", t.message.toString())
//                        }
//                    })
//
//                }
//            }
//        } else {
//
//        }
//
//
//    }

    private fun googleAutoLogin(){
        var account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            Log.e("googleAutologin",account.serverAuthCode.toString())

        }
        else{
            Log.e("googleAutologin","not yet")
        }
    }
}