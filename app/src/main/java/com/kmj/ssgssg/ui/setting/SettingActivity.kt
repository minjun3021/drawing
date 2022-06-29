package com.kmj.ssgssg.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.ActivitySettingBinding
import com.kmj.ssgssg.extension.viewBinding

class SettingActivity : AppCompatActivity() {
    private val binding: ActivitySettingBinding by viewBinding(ActivitySettingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }
}