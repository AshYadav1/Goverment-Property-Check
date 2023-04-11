package com.test.app.model.requestModel

import com.google.gson.annotations.SerializedName

data class MyModel(
    @SerializedName("data-raw"    ) var dataraw     : RequestModelHolder? = null
)
