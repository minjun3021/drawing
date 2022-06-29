package com.kmj.ssgssg.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kmj.ssgssg.databinding.ActivityMainBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    companion object{
        var firstLogin=false
    }
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

        FancyToast.makeText(this,
            if(intent.getBooleanExtra("isFirstLogined",false) ) "로그인 되었습니다" else "자동 로그인 되었습니다"
            ,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show()


        var sharedPref : SharedPreferences =applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        MainFragment.token= sharedPref.getString("token","")!!

    }
    fun haveTologin(){
        val intent =Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}