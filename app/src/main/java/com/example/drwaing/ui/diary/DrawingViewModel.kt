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

    /**
     * TODO : 여기서 날씨 / 시간 등 일기에 대한 정보를 모두 라이브데이터로 가지고있다가 save 호출하면 서버와 통신하면서 저장하도록 하면 됨
     */
    fun save(){

    }

}