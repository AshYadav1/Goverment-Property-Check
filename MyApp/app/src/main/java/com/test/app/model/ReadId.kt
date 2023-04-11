package com.test.app.model

import com.google.gson.annotations.SerializedName

data class ReadId(@SerializedName("created_at"   ) var createdAt   : String? = null,
                  @SerializedName("id"           ) var id          : Int?    = null,
                  @SerializedName("lp_id"        ) var lpId        : String? = null,
                  @SerializedName("redirect_url" ) var redirectUrl : String? = null)
