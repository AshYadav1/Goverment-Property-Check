package com.c1ctech.mvvmwithnetworksource.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.model.LoginResponse
import com.test.app.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val movieList = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<String>()
    val failure = MutableLiveData<Int>()

    fun getAllMovies(email: String, password: String) {
        val response = repository.login(email, password)
        response.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200 && response.body() != null && response.body()!!.success == true) {
                    movieList.postValue(response.body())
                } else if (response.code() == 401) {
                    failure.postValue(response.code())
                } else {
                    errorMessage.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}