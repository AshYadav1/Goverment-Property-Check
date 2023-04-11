package com.test.app.model

import com.google.gson.annotations.SerializedName


data class ReadIdHolder (

  @SerializedName("data"    ) var data    : ReadId?    = ReadId(),
  @SerializedName("errors"  ) var errors  : String?  = null,
  @SerializedName("message" ) var message : String?  = null,
  @SerializedName("status"  ) var status  : String?  = null,
  @SerializedName("success" ) var success : Boolean? = null

)