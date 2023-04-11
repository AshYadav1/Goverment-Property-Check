package com.test.app.model.requestModel

import com.google.gson.annotations.SerializedName


data class BeaconData (

  @SerializedName("timestampUnix" ) var timestampUnix : String? = null,
  @SerializedName("url"           ) var url           : String? = null

)