package com.example.mycarinventory.dto

import com.google.gson.annotations.SerializedName

data class Part(
    @SerializedName("Name") var name : String,
    @SerializedName("Model")var model : String,
    @SerializedName("Brand")var brand : String,
    @SerializedName("Make")var make : String,
    @SerializedName("Price")var price : String,)
{
    override fun toString(): String {
        return name
    }
}



