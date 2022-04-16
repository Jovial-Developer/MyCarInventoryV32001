package com.example.mycarinventory.service

import com.example.mycarinventory.RetrofitClientInstance
import com.example.mycarinventory.dao.IPartDAO
import com.example.mycarinventory.dto.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class PartService {
    suspend fun fetchParts() : List<Part>? {
        return withContext(Dispatchers.IO){
            val service = RetrofitClientInstance.retrofitInstance?.create(IPartDAO::class.java)
            val parts = async {service?.getAllParts()}
            var result = parts.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}