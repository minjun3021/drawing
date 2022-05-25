package com.example.drwaing

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    var api: NetworkInterface

    init {
         val retrofit = Retrofit.Builder()
            .baseUrl("http://146.56.130.233")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(NetworkInterface::class.java)
    }


}