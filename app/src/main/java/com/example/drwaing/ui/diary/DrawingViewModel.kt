package com.example.drwaing.ui.diary

import android.graphics.Bitmap
import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drwaing.NetworkInterface
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
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

    /**
     * TODO : 여기서 날씨 / 시간 등 일기에 대한 정보를 모두 라이브데이터로 가지고있다가 save 호출하면 서버와 통신하면서 저장하도록 하면 됨
     */
    fun save(part: MultipartBody.Part) {
        Log.e("1", "1")
        com.example.drwaing.Network.api.uploadImage(part).enqueue(object :
            retrofit2.Callback<NetworkInterface.ImageResponse> {
            override fun onResponse(
                call: Call<NetworkInterface.ImageResponse>,
                response: Response<NetworkInterface.ImageResponse>,
            ) {
                if (response.code() == 200) {
                    Log.e("uploadimage url", response.body()!!.responseMessage)
                } else {
                    Log.e("uploadimage anothercode", response.toString())
                }

            }

            override fun onFailure(call: Call<NetworkInterface.ImageResponse>, t: Throwable) {
                Log.e("uploadimage error", t.toString())
            }

        })


    }

}