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


    val diaryText = MutableLiveData<String>()

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> get() = _weather


    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> get() = _url

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
                val imageUrl = it.imageUrl
                _url.value=it.imageUrl
                Log.e("imageurl", it.imageUrl)
                upload(imageUrl)
            }.onFailure {
                it.printStackTrace()
                Log.e("image", it.toString())
            }
        }
    }


    fun upload(imageUrl: String) {
        viewModelScope.launch {
            Log.e("a","x")
            val diary = DiaryRequest(imageUrl = imageUrl,
                content = diaryText.value ?: "",
                weather = weather.value!!.name)
            kotlin.runCatching {
                Log.e("check",MainFragment.token+","+diary.toString())
                Network.api.uploadDiary(MainFragment.token, diary.content,diary.imageUrl,diary.weather)
            }.onSuccess {
                // TODO : 일기 업로드 성공 대응

                Log.e("success", "good")

            }.onFailure {
                // TODO : 일기 업로드 실패 대응
                Log.e("실패", it.toString())
                Log.e("실패",it.cause.toString())
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

                Log.e("실패", it.toString())
            }
        }

    }
}