
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Req(
    @SerializedName("apiKey") val apiKey: String? = null,
    @SerializedName("requestData") val requestData: List<RequestData?>? = null,
    @SerializedName("userName") val userName: String? = null
) {
    @Keep
    data class RequestData(
        @SerializedName("key") val key: String? = null,
        @SerializedName("value") val value: String? = null
    )
}