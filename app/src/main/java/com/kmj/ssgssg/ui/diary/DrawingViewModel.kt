package com.kmj.ssgssg.ui.diary

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.ssgssg.Network
import com.kmj.ssgssg.data.diary.DiaryApiModel
import com.kmj.ssgssg.data.diary.DiaryRequest
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.ui.main.MainFragment
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DrawingViewModel : ViewModel() {

    val timeToNavigate=MutableLiveData(false)

    val diaryText = MutableLiveData<String>()

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> get() = _weather


    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap


    private val _diary = MutableLiveData<DiaryApiModel>()
    val diary: LiveData<DiaryApiModel> get() = _diary


    fun setWeather(weather: Weather) {
        _weather.value = weather
    }

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
                Network.api.uploadImage(MainFragment.token, part)
            }.onSuccess {
                upload(it.imageUrl)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }


    fun upload(imageUrl: String) {
        viewModelScope.launch {
            val diary = DiaryRequest(imageUrl = imageUrl,
                content = diaryText.value ?: "",
                weather = weather.value!!.name)
            kotlin.runCatching {
                Network.api.uploadDiary(MainFragment.token, diary.content,diary.imageUrl,diary.weather)
            }.onSuccess {
                timeToNavigate.value=true

            }.onFailure {

            }
        }
    }

    fun getDiary(diaryId: Int) {
        viewModelScope.launch {

            kotlin.runCatching {
                Network.api.getDiary(MainFragment.token, diaryId)
            }.onSuccess {

                _diary.postValue(it)

            }.onFailure {

            }
        }

    }
}