package com.example.drwaing

import com.example.drwaing.data.diary.DiaryApiModel
import com.example.drwaing.data.diary.DiaryRequest
import com.example.drwaing.ui.main.DrawingListData
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.*

interface NetworkInterface {
    data class SignRequest(
        @SerializedName("socialToken") val socialToken: String,
        @SerializedName("socialType") val socialType: String,
    )

    data class SignResponse(
        @SerializedName("accessToken") val accessToken: String,
    )

    data class ImageResponse(
        @SerializedName("responseMessage") val responseMessage: String,
    )




    @POST("/auth/sign-up")
    suspend fun signup(@Body data: SignRequest): SignResponse

    @POST("/auth/sign-in")
    suspend fun signin(@Body data: SignRequest): SignResponse



    @Multipart
    @POST("/diary/image")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
    ): ImageResponse

    @POST("/diary")
    suspend fun uploadDiary(
        @Header("Authorization") token: String,
        @Body createDiaryRequest: DiaryRequest,
    ): ImageResponse

    @GET("/diary/list/me")
    suspend fun getMyDiaryList(
        @Header("Authorization") token: String,
        @Query("lastDiaryId") lastDiaryId: Int,
        @Query("size") size: Int,
    ): ArrayList<DrawingListData.Diary>

    @GET("/diary/{diaryId}")
    suspend fun getDiary(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId:Int
    ): DiaryApiModel
}
