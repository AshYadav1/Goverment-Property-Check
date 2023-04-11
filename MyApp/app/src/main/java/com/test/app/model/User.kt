package com.test.app.model

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("created_at"           ) var createdAt          : String?        = null,
  @SerializedName("email"                ) var email              : String?        = null,
  @SerializedName("first_name"           ) var firstName          : String?        = null,
  @SerializedName("id"                   ) var id                 : Int?           = null,
  @SerializedName("last_name"            ) var lastName           : String?        = null,
  @SerializedName("organization_id"      ) var organizationId     : Int?           = null,
  @SerializedName("parent_user_id"       ) var parentUserId       : Int?           = null,
  @SerializedName("person_id"            ) var personId           : String?        = null,
  @SerializedName("phone"                ) var phone              : String?        = null,
  @SerializedName("preferred_printer_id" ) var preferredPrinterId : String?        = null,
  @SerializedName("role_ids"             ) var roleIds            : ArrayList<Int> = arrayListOf(),
  @SerializedName("status"               ) var status             : String?        = null

)