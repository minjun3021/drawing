package com.example.drwaing.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    private val _diaryText = MutableLiveData<String>()
    val diaryText: LiveData<String> get() = _diaryText
}