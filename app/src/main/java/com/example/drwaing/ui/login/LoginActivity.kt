package com.example.drwaing.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.databinding.ActivityLoginBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBinding(ActivityLoginBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (AuthApiClient.instance.hasToken()) { //카카오 자동로그인 체크
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
            }
        }
        else {
            //로그인 필요
        }


//        UserApiClient.instance.unlink { error ->
//            if (error != null) {
//                Log.e("E", "연결 끊기 실패", error)
//            } else {
//                Log.e("E", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//            }
//  카카오 로그아웃

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        mGoogleSignInClient.signOut()
//        구글 로그아웃
    }
}