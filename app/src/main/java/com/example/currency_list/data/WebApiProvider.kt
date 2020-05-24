package com.example.currency_list.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebApiProvider {

    val webApi: WebApi = Retrofit.Builder()
        .baseUrl("https://api.nomics.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebApi::class.java)

}
