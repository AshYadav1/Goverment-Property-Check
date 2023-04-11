package com.test.app.model

import com.google.gson.annotations.SerializedName
import com.test.app.model.User


data class UsersList (@SerializedName("data"    ) var user    : ArrayList<User> = arrayListOf(),
    @SerializedName("errors"  ) var errors  : String?         = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("status"  ) var status  : String?         = null,
    @SerializedName("success" ) var success : Boolean?        = null

)