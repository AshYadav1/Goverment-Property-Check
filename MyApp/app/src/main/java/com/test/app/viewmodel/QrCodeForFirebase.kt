package com.test.app.viewmodel

import ReadQrCodeResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.model.QrResponse
import com.test.app.model.ReadIdHolder
import com.test.app.model.requestModel.*
import com.test.app.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class QrCodeForFirebase (private val repository: MainRepository) : ViewModel() {

    val movieList =  MutableLiveData<ReadQrCodeResponse>()
    val doneObserver =  MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val failure = MutableLiveData<Int>()



    fun readQrDetails(qrCode: String) {
        val response = repository.readQrCode(qrCode)
        response.enqueue(object : Callback<ReadQrCodeResponse> {
            override fun onResponse(call: Call<ReadQrCodeResponse>, response: Response<ReadQrCodeResponse>) {
//                movieList.postValue(response.body())
                if(response.code()==200 && response.body() !=null && response.body()!!.success==true)

                {
                    movieList.postValue(response.body())
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<ReadQrCodeResponse>, t: Throwable) {

            }

        })    }

    fun getDataFromText(text: String) {

        val response = repository.getLicenseId("1e6a54a75b06a5f1c149a6v81")
        response.enqueue(object : Callback<ReadIdHolder> {
            override fun onResponse(call: Call<ReadIdHolder>, response: Response<ReadIdHolder>) {
//                movieList.postValue(response.body())
                if(response.code()==200 && response.body() !=null && response.body()!!.success==true)

                {
                    readQrDetails(response.body()!!.data?.id.toString())
//                    movieList.postValue(response.body())
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<ReadIdHolder>, t: Throwable) {

            }

        })

    }

    fun hirFirebaseId(
        uuid: String,
        MAC_ADDRESS: String,
        ipAddress: String?,
        mId: String,
        mSelectedType: Int,
        mCheckInTimeId: Int,
        mCheckInTimeId1: Int,
        mUserName: String?,
        mSelectedUserId: Int,
        mUserChoosedtime: String?,
        mLocationChooseTime: String?,
        mListOfData: MutableList<QrResponse>
    ) {
        val mModel = RequestModelHolder()
        val mReqModel = RequestModel()
        val mActivityData = ActivityData()
        val mBeaconData = BeaconData()
//        val mThingData = ThingData()
        val mPersonData = PersonData()
        mActivityData.url="3hd.us/move_many"
        mActivityData.timestampUnix= Calendar.getInstance().timeInMillis.toString()





        if(mSelectedType==1)
        {
            mBeaconData.url=mCheckInTimeId.toString()


        }
        else{
            mBeaconData.url=mCheckInTimeId1.toString()

        }
        mBeaconData.timestampUnix=mLocationChooseTime

        val mThingDataList = ArrayList<ThingData>()
        for(items in mListOfData)
        {
            val mThingData = ThingData()
            mThingData.timestampUnix=items.time
            mThingData.url= items.product?.partNumber
            mThingDataList.add(mThingData)
        }


        mPersonData.url=mSelectedUserId.toString()
        mPersonData.timestampUnix=mUserChoosedtime
        mReqModel.activityData=mActivityData
        val mDataListBeacon = ArrayList<BeaconData>()
        mDataListBeacon.add(mBeaconData)


        val mPersonDataList = ArrayList<PersonData>()
        mPersonDataList.add(mPersonData)
        mReqModel.beaconData=mDataListBeacon
        mReqModel.thingData=mThingDataList
        mReqModel.personData=mPersonDataList
        mModel.data=mReqModel
        mModel.MACAddress=MAC_ADDRESS

        mModel.deviceID=mId
        mModel.transactionID=""
        mModel.latitude=0
        mModel.longitude=0
        mModel.transactionID=uuid
        mModel.IPAddress=ipAddress


        val response = repository.patchData(uuid,mModel)
        response?.enqueue(object : Callback<RequestModelHolder> {
            override fun onResponse(call: Call<RequestModelHolder>, response: Response<RequestModelHolder>) {

                if(response.code()==200 && response.body() !=null )

                {
                   moveItem(uuid)
                    doneObserver.postValue("done")
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<RequestModelHolder>, t: Throwable) {

            }

        })
    }

    private fun moveItem(uuid: String) {
        val mItem = MoveItemRequestModel()
        mItem.dataraw=uuid
//        repository.moveItem(mItem)

        val response = repository.moveItem(mItem)
        response?.enqueue(object : Callback<ReadIdHolder> {
            override fun onResponse(call: Call<ReadIdHolder>, response: Response<ReadIdHolder>) {
//                movieList.postValue(response.body())
                if(response.code()==200 && response.body() !=null)

                {
                    readQrDetails(response.body()!!.data?.id.toString())
                    doneObserver.postValue("done")
                }
                else if(response.code()==401){
                    failure.postValue(response.code())
                }
                else{
                    errorMessage.postValue(response.message())
                }

            }

            override fun onFailure(call: Call<ReadIdHolder>, t: Throwable) {

            }

        })
    }

}