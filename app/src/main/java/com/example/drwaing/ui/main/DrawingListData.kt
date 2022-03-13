package com.example.drwaing.ui.main

/**
 * sealed class 공부하기
 * enum class / interface 등과 어떻게 다른지
 * 장점은 무엇인지
 * */
sealed class DrawingListData {
    object Header : DrawingListData()

    // TODO : 리스트 스펙 받아서 구현
    data class DrawingData(
        val data: Any
    ) : DrawingListData()
}