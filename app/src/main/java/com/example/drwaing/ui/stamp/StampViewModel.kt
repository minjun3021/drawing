package com.example.drwaing.ui.stamp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drwaing.Network
import com.example.drwaing.data.diary.DiaryApiModel
import com.example.drwaing.data.diary.Stamp
import com.example.drwaing.ui.main.DrawingListData
import com.example.drwaing.ui.main.MainFragment
import kotlinx.coroutines.launch

class StampViewModel : ViewModel() {
    private val _diary = MutableLiveData<DiaryApiModel>()
    val diary: LiveData<DiaryApiModel> get() = _diary

    private val _stamps = MutableLiveData<ArrayList<Stamp>>()
    val stamps: LiveData<ArrayList<Stamp>> get() = _stamps

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
    fun sendStamp(diaryId: Int,stampType :String,x:Double,y:Double) {
        viewModelScope.launch {

            kotlin.runCatching {
                Network.api.addStamp(MainFragment.token, diaryId,stampType,x,y)
            }.onSuccess {
                Log.e("도장","찍힘")
            }.onFailure {

                Log.e("실패", it.toString())
            }
        }

    }
}