package com.example.drwaing

import com.kakao.sdk.auth.model.OAuthToken
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    suspend fun signup(@Body data : SignRequest): SignResponse

    @POST("/auth/sign-in")
    suspend fun signin(@Body data: SignRequest): SignResponse

    @Multipart
    @POST("/diary/image")
    fun uploadImage(
        @Part file : MultipartBody.Part?
    ) : Call<ImageResponse>
}
