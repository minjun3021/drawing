package com.example.drwaing.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    val diaryText = MutableLiveData<String>()

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> get() = _date


}