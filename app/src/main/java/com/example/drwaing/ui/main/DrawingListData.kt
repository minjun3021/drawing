package com.example.drwaing.ui.main

import com.google.gson.annotations.SerializedName

/**
 * sealed class 공부하기
 * enum class / interface 등과 어떻게 다른지
 * 장점은 무엇인지
 * */

//sealed를 붙이면 추상클래스가 되므로 하위클래스가 상속받아야함
//Sealed Class는 동일 파일에 정의된 하위 클래스 외에는 상속받는 클래스가 존재하지 않는다는 것을 컴파일러에 알려준다
//eum class로도 비슷한효과를 낼수있지만 상속받을수있는 하위 클래스가 1개로 제한된다.


//object 더 공부하기
sealed class DrawingListData {

    object Header : DrawingListData()

    data class Diary(
        @SerializedName("createdDate") val createdDate: String,
        @SerializedName("diaryId") val diaryId: Int,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("weather") val weather: String,
    ): DrawingListData()
}