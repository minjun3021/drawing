package com.example.drwaing

import android.app.Application
import com.example.drwaing.view.draw.UIKit
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //KakaoSdk.init(this, "6f9ac5ea25cd0407780a48daa5af9a1d")
        KakaoSdk.init(this, "581fc808602a8f2c67218d7aa03ffa2f")

        UIKit.initialize(this)
    }
}