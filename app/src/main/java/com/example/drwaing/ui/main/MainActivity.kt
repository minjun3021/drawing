package com.example.drwaing.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.databinding.ActivityMainBinding
import com.example.drwaing.extension.viewBinding
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("카카오정보실패", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.e("정보", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }
}