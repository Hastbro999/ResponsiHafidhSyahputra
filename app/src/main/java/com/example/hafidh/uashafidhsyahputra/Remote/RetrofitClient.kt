package com.example.hafidh.uashafidhsyahputra.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// retrofit client untuk menghubungkan ke server
object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return  retrofit!!
    }
}