package com.kmj.ssgssg.ui.stamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.ssgssg.Network
import com.kmj.ssgssg.data.diary.DiaryApiModel
import com.kmj.ssgssg.data.diary.Stamp
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.ui.main.MainFragment
import kotlinx.coroutines.launch

class StampViewModel : ViewModel() {
    private val _diary = MutableLiveData<DiaryApiModel>()
    val diary: LiveData<DiaryApiModel> get() = _diary

    val timeToNavigate=MutableLiveData(false)



    fun getRandomDiary() {
        viewModelScope.launch {

            kotlin.runCatching {
                Network.api.getRandomDiary(MainFragment.token)
            }.onSuccess {

                _diary.postValue(it)
            }.onFailure {

            }
        }

    }
    fun sendStamp(diaryId: Int,stampType :String,x:Double,y:Double) {
        viewModelScope.launch {

            kotlin.runCatching {
                Network.api.addStamp(MainFragment.token, diaryId,stampType,x,y)
            }.onSuccess {
                timeToNavigate.value=true

            }.onFailure {

            }
        }

    }
}