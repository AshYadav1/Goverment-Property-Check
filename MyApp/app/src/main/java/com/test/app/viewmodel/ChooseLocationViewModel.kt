package com.test.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.model.*
import com.test.app.repository.MainRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseLocationViewModel(private val repository: MainRepository) : ViewModel() {

    val movieList = MutableLiveData<List<Data>>()
    val errorMessage = MutableLiveData<String>()
    val failure = MutableLiveData<Int>()

    fun getAllMovies() {

        val response = repository.getLocation()
        response.enqueue(object :Callback<LocationItemsHolder>{
            override fun onResponse(call: Call<LocationItemsHolder>, response: Response<LocationItemsHolder>) {
//                 movieList.postValue(response.body()?.data)

                if(response.code()==200 && response.body() !=null && response.body()!!.success==true)

                {
                    movieList.postValue(response.body()!!.data)
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                 else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<LocationItemsHolder>, t: Throwable) {

            }

        })
//         response?.enqueue(object :Callback<ResponseBody?>{
//             override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
////                 TODO("Not yet implemented")
//             }
//
//             override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
////                 TODO("Not yet implemented")
//             }
//
//         })
//        response.enqueue(object : Callback<Res> {
//            override fun onResponse(call: Call<LocationItemsHolder>, response: Response<LocationItemsHolder>) {
////                movieList.postValue(response.body())
//            }
//
//            override fun onFailure(call: Call<LocationItemsHolder>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//        })
    }
}