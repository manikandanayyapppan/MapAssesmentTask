package com.example.mapassesmenttask.core.data.model

data class MapResponse(
    val id: String?,
    val name: String?,
    val data: Data?
)

data class Data(
    val year: Int?,
    val price: Double?,
    val capacity: String?
)