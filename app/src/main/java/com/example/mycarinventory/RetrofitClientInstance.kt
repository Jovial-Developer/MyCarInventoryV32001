package com.example.mycarinventory

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofitInstance : Retrofit?
    get() {
        //has been created YES/NO?
        if(retrofit == null){
            //create
            retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}