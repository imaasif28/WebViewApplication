
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Req(
    @SerializedName("apiKey") val apiKey: String? = null,
    @SerializedName("requestData") val requestData: List<Any?>? = null,
    @SerializedName("userName") val userName: String? = null
)