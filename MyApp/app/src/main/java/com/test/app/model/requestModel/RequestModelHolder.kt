package com.test.app.model.requestModel

import com.google.gson.annotations.SerializedName


data class RequestModelHolder (

  @SerializedName("IP_address"    ) var IPAddress     : String? = null,

  @SerializedName("MAC_address"   ) var MACAddress    : String? = null,
  @SerializedName("data"          ) var data          : RequestModel?   = RequestModel(),
  @SerializedName("deviceID"      ) var deviceID      : String? = null,
  @SerializedName("latitude"      ) var latitude      : Int?    = null,
  @SerializedName("longitude"     ) var longitude     : Int?    = null,
  @SerializedName("transactionID" ) var transactionID : String? = null

)