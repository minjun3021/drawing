package com.example.drwaing.ui.main

sealed class DrawingListData {
    object Header : DrawingListData()

    data class DrawingData(
        val data: Any
    ) : DrawingListData()
}