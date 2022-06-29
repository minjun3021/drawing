package com.kmj.ssgssg.ui.login

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kmj.ssgssg.databinding.ActivityLoginBinding
import com.kmj.ssgssg.extension.viewBinding


class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by viewBinding(ActivityLoginBinding::inflate)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }

}