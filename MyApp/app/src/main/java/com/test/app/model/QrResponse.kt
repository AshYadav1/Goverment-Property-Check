package com.test.app.model

import Product
import com.google.gson.annotations.SerializedName

data class QrResponse(@SerializedName("created_at"              ) var createdAt            : String?           = null,
                      @SerializedName("external_serial_number"  ) var externalSerialNumber : String?           = null,
                      @SerializedName("id"                      ) var id                   : Int?              = null,
                      @SerializedName("time"                      ) var time                   : String?              = null,
                      @SerializedName("is_consumer_facing"      ) var isConsumerFacing     : Boolean?          = null,
                      @SerializedName("license_plate_move"      ) var licensePlateMove     : ArrayList<String> = arrayListOf(),
                      @SerializedName("location"                ) var location             : Int?              = null,
                      @SerializedName("location_id"             ) var locationId           : Int?              = null,
                      @SerializedName("lp_id"                   ) var lpId                 : String?           = null,
                      @SerializedName("organization_id"         ) var organizationId       : Int?              = null,
                      @SerializedName("parent_license_plate_id" ) var parentLicensePlateId : String?           = null,
                      @SerializedName("product"                 ) var product              : Product?          = Product(),
                      @SerializedName("product_id"              ) var productId            : Int?              = null,
                      @SerializedName("quantity"                ) var quantity             : Int?              = null,
                      @SerializedName("redirect_url"            ) var redirectUrl          : String?           = null,
                      @SerializedName("status"                  ) var status               : String?           = null)
