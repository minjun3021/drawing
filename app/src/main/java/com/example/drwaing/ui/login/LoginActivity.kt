package com.example.drwaing.ui.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.databinding.ActivityLoginBinding
import com.example.drwaing.extension.viewBinding
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBinding(ActivityLoginBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("E", "연결 끊기 실패", error)
            } else {
                Log.e("E", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
}