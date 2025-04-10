package com.example.mapassesmenttask.core.domain.utils

import com.example.mapassesmenttask.core.data.model.Status

class Resource<out T>(
    val status: Status,
    val data: T?,
    val msg: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg) // ✅ Fixed: Pass `data` instead of null
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null) // ✅ Added loading state
        }
    }
}