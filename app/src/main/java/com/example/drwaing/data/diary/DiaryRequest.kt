package com.example.drwaing.data.diary

import com.google.gson.annotations.SerializedName

data class DiaryRequest(
    @SerializedName("content") val content: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("weather") val weather: String,
)


enum class Weather (val weather : String){
    SUN("SUN"),
    CLOUD("CLOUD"),
    RAIN("RAIN"),
    SNOW("SNOW")
}