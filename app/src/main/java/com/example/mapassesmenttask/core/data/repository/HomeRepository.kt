package com.example.mapassesmenttask.core.data.repository

import com.example.mapassesmenttask.core.data.manager.Mapper
import com.example.mapassesmenttask.core.data.model.MapResponse
import com.example.mapassesmenttask.core.data.network.ApiService
import com.example.mapassesmenttask.core.domain.utils.Resource

class HomeRepository(
    private val apiService: ApiService
) {

    suspend fun getAllMovies(): Resource<List<MapResponse?>?> {
        return Mapper.map(apiService.getMapList())
    }
}