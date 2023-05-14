package com.test.app.viewmodel

import ReadQrCodeResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.model.User
import com.test.app.model.UsersList
import com.test.app.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowLocationDataViewModel(private val repository: MainRepository) : ViewModel() {

    val movieList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()
    val failure = MutableLiveData<Int>()
    val userIdCaptured = MutableLiveData<Int>()


    fun getAllMovies() {

//        val response = repository.getUsers()
//        response.enqueue(object : Callback<UsersList> {
//            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
//                movieList.postValue(response.body()?.user)
//            }
//
//            override fun onFailure(call: Call<UsersList>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//        })

        val response = repository.getUsers()
        response.enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.code() == 200 && response.body() != null && response.body()!!.success == true) {
                    movieList.postValue(response.body()!!.user)
                } else if (response.code() == 401) {
                    failure.postValue(response.code())
                } else {
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {

            }

        })
    }


    fun readUserDetail( qrCode: String) {
        val response = repository.readUserDetailByQr(qrCode)
        response.enqueue(object : Callback<ReadQrCodeResponse> {
            override fun onResponse(
                call: Call<ReadQrCodeResponse>,
                response: Response<ReadQrCodeResponse>
            ) {
                if (response.code() == 200 && response.body() != null && response.body()!!.success == true) {
                    userIdCaptured.value= response.body()!!.data!!.id!!
                } else if (response.code() == 401) {
                    failure.postValue(response.code())
                    errorMessage.postValue(response.message())
                } else {
                    errorMessage.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<ReadQrCodeResponse>, t: Throwable) {

            }
        })
    }

}