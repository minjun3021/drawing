package com.kmj.ssgssg

import com.kmj.ssgssg.data.diary.DiaryApiModel
import com.kmj.ssgssg.ui.main.DrawingListData
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

    data class Response(
        @SerializedName("responseMessage") val responseMessage: String,
    )
    data class ImageResponse(
        @SerializedName("imageUrl") val imageUrl: String,
    )


    @POST("/auth/leave")
    suspend fun leave(
        @Header("Authorization") token: String
    )

    @POST("/auth/sign-up")
    suspend fun signup( @Query("socialToken") socialToken: String,
                        @Query("socialType") socialType :String
    ): SignResponse

    @POST("/auth/sign-in")
    suspend fun signin(
        @Query("socialToken") socialToken: String,
        @Query("socialType") socialType :String
    ): SignResponse


    @POST("/auth/sign-up")
    suspend fun signupwithgoogle( @Query("idToken") idToken: String,
                        @Query("socialType") socialType :String

    ): SignResponse
    @POST("/auth/sign-in")
    suspend fun signinwithgoogle(
        @Query("idToken") idToken: String,
        @Query("socialType") socialType :String
    ): SignResponse



    @Multipart
    @POST("/diary/image")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?,
    ): ImageResponse


    @POST("/diary")
    suspend fun uploadDiary(
        @Header("Authorization") token: String,
        @Query("content") content: String,
        @Query("imageUrl") imageUrl: String,
        @Query("weather") weather :String
    ): DrawingListData.Diary

    @GET("/diary/list/me")
    suspend fun getMyDiaryList(
        @Header("Authorization") token: String,
        @Query("lastDiaryId") lastDiaryId: Int,
        @Query("size") size: Int,
    ): ArrayList<DrawingListData.Diary>

    @GET("/diary/list")
    suspend fun getDiaryList(
        @Header("Authorization") token: String,
        @Query("lastDiaryId") lastDiaryId: Int,
        @Query("size") size: Int,
    ): ArrayList<DrawingListData.Diary>

    @GET("/diary/{diaryId}")
    suspend fun getDiary(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId:Int
    ): DiaryApiModel

    @POST("/stamp")
    suspend fun addStamp(
        @Header("Authorization") token: String,
        @Query("diaryId") diaryId: Int,
        @Query("stampType") stampType: String,
        @Query("x") x: Double,
        @Query("y") y :Double
    ): Response
}
