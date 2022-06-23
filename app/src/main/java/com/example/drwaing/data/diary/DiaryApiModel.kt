package com.example.drwaing.data.diary


import com.google.gson.annotations.SerializedName

data class DiaryApiModel(
    @SerializedName("content")
    val content :String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("diaryId")
    val diaryId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("stampList")
    val stampList: ArrayList<Stamp>,
    @SerializedName("weather")
    val weather: String
)