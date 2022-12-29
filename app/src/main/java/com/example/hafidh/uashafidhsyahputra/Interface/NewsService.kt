package com.example.hafidh.uashafidhsyahputra.Interface

import com.example.hafidh.uashafidhsyahputra.Model.News
import com.example.hafidh.uashafidhsyahputra.Model.WebSite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

// news service untuk mengambil data dari API
interface NewsService {
    @get: GET("v2/sources?apiKey=7cc6f9c8dfa44fe993467007ada10a9e")
    val sources: Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url: String): Call<News>
}