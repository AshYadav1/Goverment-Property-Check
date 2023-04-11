package com.test.app.model

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("email"   ) var email   : String?           = null,
    @SerializedName("password"   ) var password   : String?           = null
)
