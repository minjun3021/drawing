package com.example.drwaing

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.annotation.RequiresApi
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("E", "연결 끊기 실패", error)
            }
            else {
                Log.e("E", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }

    }

}
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "6f9ac5ea25cd0407780a48daa5af9a1d")
    }
}