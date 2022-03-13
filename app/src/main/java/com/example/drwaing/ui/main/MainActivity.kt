package com.example.drwaing.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.databinding.ActivityMainBinding
import com.example.drwaing.extension.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}