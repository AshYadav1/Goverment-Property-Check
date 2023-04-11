package com.test.app.model.requestModel

import com.google.gson.annotations.SerializedName

data class MoveItemRequestModel(
    @SerializedName("transactionID"    ) var dataraw     : String? = null
)
