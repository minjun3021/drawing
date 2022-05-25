package com.example.drwaing

import com.kakao.sdk.auth.model.OAuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkInterface {
    data class SignRequest(
        val socialToken: String,
        val socialType: String
    )
    data class SignResponse(
        val accessToken : String
    )



    @POST("/auth/sign-up")
    fun signup(@Body data : SignRequest): Call<SignResponse>

    @POST("/auth/sign-in")
    fun signin(@Body data: SignRequest): Call<SignResponse>
}