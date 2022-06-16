package com.example.drwaing.ui.main

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drwaing.Network
import com.example.drwaing.NetworkInterface
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainViewModel : ViewModel() {
    private val _diaryList = MutableLiveData<ArrayList<DrawingListData>>()
    val diaryList: LiveData<ArrayList<DrawingListData>> get() = _diaryList

    fun getMyDiaryList() {

        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.getMyDiaryList(MainFragment.token,0,1000)
            }.onSuccess {
                _diaryList.postValue(it as ArrayList<DrawingListData>)
            }.onFailure {
                it.printStackTrace()
                Log.e("fuck",it.toString())
            }
        }
    }
}