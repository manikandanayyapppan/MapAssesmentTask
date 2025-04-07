package com.example.mapassesmenttask.core.data.manager

import com.example.mapassesmenttask.core.data.model.MapResponse
import com.example.mapassesmenttask.core.domain.utils.Resource
import retrofit2.Response

class Mapper {
    companion object {
        fun map(response: Response<List<MapResponse?>>): Resource<List<MapResponse?>> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.success(body)
                } else {
                    Resource.error("Empty response", null)
                }
            } else {
                val errorMsg = "Error ${response.code()}: ${response.message()}"
                Resource.error(errorMsg, null)
            }
        }
    }
}