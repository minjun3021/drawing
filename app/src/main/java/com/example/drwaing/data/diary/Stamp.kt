package com.example.drwaing.data.diary


import com.google.gson.annotations.SerializedName

data class Stamp(
    @SerializedName("stampId")
    val stampId: Int,
    @SerializedName("stampType")
    val stampType: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)