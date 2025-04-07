package com.example.mapassesmenttask.ui.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.mapassesmenttask.core.data.model.MapResponse
import com.example.mapassesmenttask.core.data.model.Status
import com.example.mapassesmenttask.core.data.network.NetworkHelper
import com.example.mapassesmenttask.core.data.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {



    private val _messageLD = MutableLiveData<MyStringType>()
    val message: LiveData<MyStringType> get() = _messageLD

    private val mapResponseMLD=MutableLiveData<List<MapResponse?>>()
     val mapResponseLD : MutableLiveData<List<MapResponse?>> get() =mapResponseMLD


    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
           if (!networkHelper.isNetworkConnected()) {
             //   _messageLD.postValue(MyStringType.StringResource(R.string.msg_no_internet))
                return@launch
            }
            try {
                val response = homeRepository.getAllMovies()

                when (response.status) {
                    Status.SUCCESS -> {
                        Log.e("mani","${response.data}")
                        mapResponseMLD.postValue(
                            response.data ?: listOf()
                        )
                    }
                    Status.ERROR -> {
                        _messageLD.postValue(MyStringType.StringRaw(response.msg.orEmpty()))
                    }
                    Status.LOADING -> {  }
                }
            } catch (e: Exception) {
                _messageLD.postValue(
                    MyStringType.StringRaw(
                        e.localizedMessage ?: "An error occurred"
                    )
                )
            }
        }
    }
}

sealed class MyStringType {
    data class StringResource(@StringRes val id: Int) : MyStringType()
    data class StringRaw(val message: String) : MyStringType()
}