package com.test.app.repository

import android.content.Context
import com.test.app.MyApplication
import com.test.app.api.RetrofitService
import com.test.app.model.requestModel.MoveItemRequestModel
import com.test.app.model.requestModel.RequestModelHolder


class MainRepository(private val retrofitService: RetrofitService,
                     private val retrofitServiceForFirebase: RetrofitService?,private val restApiForMoveItem: RetrofitService?=null
) {

    fun getAllMovies() = retrofitService.getAllMovies()
    fun login(email: String, password:String) = retrofitService.login(email,password)
    fun getLocation()=retrofitService.getAllLocation()
    fun getUsers() =retrofitService.getUsers()
    fun readQrCode(qrCode: String)= retrofitService.readQrCode(
        MyApplication.appContext.getSharedPreferences(
            MyApplication.appContext.packageName, Context.MODE_PRIVATE).getString("org_slug",""),qrCode)
    fun getLicenseId(qrCode: String)=retrofitService.getLicenseId(qrCode)
    fun patchData(qrCode: String,model:RequestModelHolder)=retrofitServiceForFirebase?.patchFirebase(qrCode,model)
    fun moveItem(uuid: MoveItemRequestModel) =restApiForMoveItem?.moveItem(uuid)
    fun firebaseSerial(serialCode: String?) =retrofitServiceForFirebase?.verifySerialNumber(serialCode)
}