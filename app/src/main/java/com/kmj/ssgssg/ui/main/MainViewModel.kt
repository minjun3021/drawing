package com.kmj.ssgssg.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmj.ssgssg.Network
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _diaryList = MutableLiveData<ArrayList<DrawingListData>>()
    val diaryList: LiveData<ArrayList<DrawingListData>> get() = _diaryList

    fun getMyDiaryList() {

        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.getMyDiaryList(MainFragment.token,0,1000)
            }.onSuccess {

                    (it as ArrayList<DrawingListData>).add(0,DrawingListData.Header)
                    _diaryList.postValue(it as ArrayList<DrawingListData>)


            }.onFailure {
                it.printStackTrace()
            }
        }
    }
    fun getDiaryList(){
        viewModelScope.launch {
            kotlin.runCatching {
                Network.api.getDiaryList(MainFragment.token,0,1000)
            }.onSuccess {

                Log.e("check",(it as ArrayList<DrawingListData>).size.toString())
//                (it as ArrayList<DrawingListData>).add(0,DrawingListData.Header)
//                _diaryList.postValue(it as ArrayList<DrawingListData>)


            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}