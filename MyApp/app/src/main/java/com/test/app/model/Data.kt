package com.test.app.model
import com.google.gson.annotations.SerializedName


data class Data  (

  @SerializedName("active"             ) var active           : Boolean? = null,
  @SerializedName("beacon_id"          ) var beaconId         : String?  = null,
  @SerializedName("created_at"         ) var createdAt        : String?  = null,
  @SerializedName("depth"              ) var depth            : String?  = null,
  @SerializedName("height"             ) var height           : String?  = null,
  @SerializedName("id"                 ) var id               : Int?     = null,
  @SerializedName("location_type"      ) var locationType     : String?  = null,
  @SerializedName("name"               ) var name             : String?  = null,
  @SerializedName("organization_id"    ) var organizationId   : Int?     = null,
  @SerializedName("parent_location_id" ) var parentLocationId : String?  = null,
  @SerializedName("unit"               ) var unit             : String?  = null,
  @SerializedName("width"              ) var width            : String?  = null

)