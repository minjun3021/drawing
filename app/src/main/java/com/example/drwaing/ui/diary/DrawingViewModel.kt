package com.example.drwaing.ui.diary

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    val diaryText = MutableLiveData<String>()

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> get() = _date

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap

    fun saveBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

}