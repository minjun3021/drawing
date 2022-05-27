package com.example.drwaing.ui.diary

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drwaing.Network
import com.example.drwaing.data.diary.DiaryRequest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DrawingViewModel : ViewModel() {
    val diaryText = MutableLiveData<String>()

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> get() = _date

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap


    fun saveBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun save(file: File) {
        val part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.uploadImage(part)
            }.onSuccess {
                val imageUrl = it.responseMessage
                upload(imageUrl)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }


    //TODO : 날씨 선택 기능 추가해서 weather 변수 수정
    fun upload(imageUrl: String){
        viewModelScope.launch {
            val diary = DiaryRequest(imageUrl = imageUrl, content = diaryText.value ?: "", weather = "SNOW")
            kotlin.runCatching {
                Network.api.uploadDiary(diary)
            }.onSuccess {
                // TODO : 일기 업로드 성공 대응
            }.onFailure {
                // TODO : 일기 업로드 실패 대응
            }
        }
    }
}