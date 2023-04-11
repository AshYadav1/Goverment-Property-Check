package com.test.app.model

import com.google.gson.annotations.SerializedName

data class LoginDataModel(  @SerializedName("access_token"  ) var accessToken  : String? = null,
                            @SerializedName("org_slug"      ) var orgSlug      : String? = null,
                            @SerializedName("refresh_token" ) var refreshToken : String? = null)