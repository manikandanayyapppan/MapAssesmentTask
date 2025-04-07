package com.example.mapassesmenttask.core.data.network

import com.example.mapassesmenttask.core.data.model.MapResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("objects")
    suspend fun getMapList(): Response<List<MapResponse?>>
}

