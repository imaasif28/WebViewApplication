
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Res(
    @SerializedName("apiKey") val apiKey: String? = null,
    @SerializedName("isLogin") val isLogin: Boolean? = null,
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("redirectUrl") val redirectUrl: String? = null,
    @SerializedName("responseCode") val responseCode: Int? = null,
    @SerializedName("userName") val userName: String? = null
)