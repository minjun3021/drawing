package com.example.drwaing.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivityMainBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.diary.DiaryActivity
import com.example.drwaing.ui.login.LoginActivity
import com.example.drwaing.ui.login.SocialLoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AutoLoginCheck()
    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun kakaoAutoLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        haveTologin()
                    } else {
                        haveTologin()
                    }
                } else { //토큰이 있는 경우
                    whenHaveToken()
                }
            }
        } else {
            haveTologin()
        }


    }

    private fun AutoLoginCheck(){
        var account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){

            whenHaveToken()

        }
        else{
            kakaoAutoLogin()
        }
    }
    fun whenHaveToken(){
        var sharedPref : SharedPreferences =applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        MainFragment.token= sharedPref.getString("token","")!!

    }
    fun haveTologin(){
        val intent =Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}