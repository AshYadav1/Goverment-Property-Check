package com.test.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.model.*
import com.test.app.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowLocationDataViewModel (private val repository: MainRepository) : ViewModel() {

    val movieList =  MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()
    val failure = MutableLiveData<Int>()


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
        response.enqueue(object :Callback<UsersList>{
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if(response.code()==200 && response.body() !=null && response.body()!!.success==true)

                {
                    movieList.postValue(response.body()!!.user)
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {

            }

        })
    }
}