package com.test.app.api


import ReadQrCodeResponse
import android.content.Context
import com.test.app.MyApplication
import com.test.app.model.*
import com.test.app.model.requestModel.MoveItemRequestModel
import com.test.app.model.requestModel.RequestModelHolder
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("api/{org_slug}/locations/")
    fun getAllLocation(@Path("org_slug") orgSlug: String? =  MyApplication.appContext.getSharedPreferences(MyApplication.appContext.packageName, Context.MODE_PRIVATE).getString("org_slug","")): Call<LocationItemsHolder>
    @GET("{org_slug}/api/lexcorp/locations/")
    fun getAllMovies(  @Path("org_slug") orgSlug: String? =  MyApplication.appContext.getSharedPreferences(MyApplication.appContext.packageName, Context.MODE_PRIVATE).getString("org_slug","")): Call<MovieList>

    @FormUrlEncoded
    @POST("auth/moment-track/system/login")
    fun login(@Field("email") email: String,@Field("password") password: String): Call<LoginResponse>

//    https://api-dev.momenttrack.com//api/lexcorp/locations/


    @GET("api/{org_slug}/users/")
    fun getUsers( @Path("org_slug") orgSlug: String? =  MyApplication.appContext.getSharedPreferences(MyApplication.appContext.packageName, Context.MODE_PRIVATE).getString("org_slug","")): Call<UsersList>


    @GET("api/{org_slug}/license_plates/{license_plates}")
    fun readQrCode( @Path("org_slug") orgSlug: String? =  MyApplication.appContext.getSharedPreferences(MyApplication.appContext.packageName, Context.MODE_PRIVATE).getString("org_slug",""),@Path("license_plates")  id :String): Call<ReadQrCodeResponse>
    @GET("publicapi/license_plates/lookup")
    fun getLicenseId(@Query("lp_id")  id :String): Call<ReadIdHolder>



    @PATCH("/transaction/{id}.json")
    fun patchFirebase(@Path("id") qrCode: String,@Body model: RequestModelHolder): Call<RequestModelHolder>

    @POST("app/moveitem.moveitem")
    fun moveItem(@Body model: MoveItemRequestModel): Call<ReadIdHolder>


    companion object {

//       httpClient : OkHttpClient? by lazy {
//
//        }

        var retrofitService: RetrofitService? = null

        //Create the RetrofitService instance using the retrofit.
        fun getInstance(): RetrofitService {

//            if (retrofitService == null) {
////                val retrofit = Retrofit.Builder()
////                    .baseUrl("https://api-dev.momenttrack.com//")
////                    .addConverterFactory(GsonConverterFactory.create())
////                    .client(ApiClient.httpClient)
////                    .build()
//                retrofitService = ApiClient.retrofit.create(RetrofitService::class.java)
//            }
            retrofitService = ApiClient.retrofit.create(RetrofitService::class.java)
            return retrofitService!!
        }
        fun getInstanceForFirebase(): RetrofitService {

            retrofitService = ApiClient.retrofitFirebase.create(RetrofitService::class.java)
            return retrofitService!!
        }
        fun getInstanceForMoveItem(): RetrofitService {

            retrofitService = ApiClient.retrofitForMoveItem.create(RetrofitService::class.java)
            return retrofitService!!
        }
    }

}