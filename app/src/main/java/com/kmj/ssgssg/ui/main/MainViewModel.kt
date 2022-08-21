package com.kmj.ssgssg.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.ssgssg.Network
import com.kmj.ssgssg.NetworkInterface
import com.kmj.ssgssg.data.diary.DiaryApiModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    val check = MutableLiveData(0)

    private val _diaryList = MutableLiveData<ArrayList<DrawingListData>>()
    val diaryList: LiveData<ArrayList<DrawingListData>> get() = _diaryList

    private val _newStampedDiary = MutableLiveData<ArrayList<DiaryApiModel>>()
    val newStampedDiary: LiveData<ArrayList<DiaryApiModel>> get() = _newStampedDiary

    fun getMyDiaryList() {

        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.getMyDiaryList(MainFragment.token, 0, 1000)
            }.onSuccess {

                (it as ArrayList<DrawingListData>).add(0, DrawingListData.Header)
                _diaryList.postValue(it as ArrayList<DrawingListData>)


            }.onFailure {
                check.value=3
                if (it is HttpException) {
                    when (it.code()) {
                        500 -> {
                            kotlin.runCatching {
                                Network.api.refreshToken(NetworkInterface.RefreshRequest(MainFragment.refreshToken))
                            }.onSuccess { it ->
                                MainFragment.token=it.accessToken
                                check.value=1

                            }.onFailure {

                                check.value=2
                            }
                        }
                    }
                }
            }
        }
    }

    fun getNewStampedDiary() {

        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.getNewStampedDiary(MainFragment.token)
            }.onSuccess {

                _newStampedDiary.postValue(it)

                viewModelScope.launch {
                    kotlin.runCatching {
                        Network.api.updateAccessTime(MainFragment.token)
                    }.onSuccess {

                    }.onFailure {

                    }
                }

            }.onFailure {
                it.printStackTrace()
            }
        }
    }


}