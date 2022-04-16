package com.example.mycarinventory.dao

import com.example.mycarinventory.dto.Part
import retrofit2.Call
import retrofit2.http.GET

interface IPartDAO {
    @GET("")
    fun getAllParts() : Call<ArrayList<Part>>
}