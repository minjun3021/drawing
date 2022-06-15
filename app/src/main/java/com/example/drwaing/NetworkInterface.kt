package com.example.drwaing

import com.example.drwaing.data.diary.DiaryRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface NetworkInterface {
    data class SignRequest(
        val socialToken: String,
        val socialType: String
    )
    data class SignResponse(
        val accessToken : String
    )

    data class ImageResponse(
        val responseMessage :String
    )


    @POST("/auth/sign-up")
    suspend fun signup(@Body data: SignRequest): SignResponse

    @POST("/auth/sign-in")
    suspend fun signin(@Body data: SignRequest): SignResponse

    @Multipart
    @POST("/diary/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part?
    ): ImageResponse

    @POST("/diary")
    @FormUrlEncoded
    suspend fun uploadDiary(
        @Field("createDiaryRequest") createDiaryRequest: DiaryRequest
    ): ImageResponse
}
