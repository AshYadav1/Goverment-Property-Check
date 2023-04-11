package com.test.app.model

import com.google.gson.annotations.SerializedName

data class FailureResponseKotlin (
     @SerializedName("IsSuccess" ) var IsSuccess : Boolean?          = null,
    @SerializedName("Message"   ) var Message   : String?           = null,

    @SerializedName("Errors"    ) var Errors    : ArrayList<Exception> = arrayListOf()
)
