
import com.google.gson.annotations.SerializedName
import com.test.app.model.QrResponse


data class ReadQrCodeResponse (

  @SerializedName("data"    ) var data    : QrResponse?    = QrResponse(),
  @SerializedName("errors"  ) var errors  : String?  = null,
  @SerializedName("message" ) var message : String?  = null,
  @SerializedName("status"  ) var status  : String?  = null,
  @SerializedName("success" ) var success : Boolean? = null

)