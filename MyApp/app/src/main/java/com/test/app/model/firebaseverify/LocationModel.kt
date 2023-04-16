package com.test.app.model.firebaseverify

import com.google.gson.annotations.SerializedName

data class LocationModel(
    val type: String,
    @SerializedName("Status")
    val status: Boolean,
)
