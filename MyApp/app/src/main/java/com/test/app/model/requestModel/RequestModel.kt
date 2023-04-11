package com.test.app.model.requestModel

import com.google.gson.annotations.SerializedName

data class RequestModel(@SerializedName("activityData" ) var activityData : ActivityData?         = ActivityData(),
                        @SerializedName("beaconData"   ) var beaconData   : ArrayList<BeaconData> = arrayListOf(),
                        @SerializedName("personData"   ) var personData   : ArrayList<PersonData> = arrayListOf(),
                        @SerializedName("thingData"    ) var thingData    : ArrayList<ThingData>  = arrayListOf())
