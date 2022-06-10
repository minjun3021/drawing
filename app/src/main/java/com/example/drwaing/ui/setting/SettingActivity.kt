package com.example.drwaing.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivityMainBinding
import com.example.drwaing.databinding.ActivitySettingBinding
import com.example.drwaing.extension.viewBinding

class SettingActivity : AppCompatActivity() {
    private val binding: ActivitySettingBinding by viewBinding(ActivitySettingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }
}