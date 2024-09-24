
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class VerTenantRes(
    @SerializedName("isLogin") val isLogin: Boolean? = null,
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("responseCode") val responseCode: Int? = null,
    @SerializedName("responseData") val responseData: ResponseData? = null,
    @SerializedName("userName") val userName: String? = null
) {
    @Keep
    data class ResponseData(
        @SerializedName("TenantInfo") val tenantInfo: TenantInfo? = null,
        @SerializedName("userInfo") val userInfo: UserInfo? = null
    ) {
        @Keep
        data class TenantInfo(
            @SerializedName("apiKey") val apiKey: String? = null,
            @SerializedName("favIconFilePath") val favIconFilePath: String? = null,
            @SerializedName("logoFilePath") val logoFilePath: String? = null,
            @SerializedName("menu") val menu: List<Menu?>? = null,
            @SerializedName("tenantCode") val tenantCode: String? = null,
            @SerializedName("tenantId") val tenantId: Int? = null,
            @SerializedName("tenantName") val tenantName: String? = null,
            @SerializedName("themeName") val themeName: String? = null
        ) {
            @Keep
            data class Menu(
                @SerializedName("id") val id: Int? = null,
                @SerializedName("link") val link: String? = null,
                @SerializedName("name") val name: String? = null
            )
        }

        @Keep
        data class UserInfo(
            @SerializedName("city") val city: Any? = null,
            @SerializedName("customerName") val customerName: String? = null,
            @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
            @SerializedName("emailId") val emailId: String? = null,
            @SerializedName("firstName") val firstName: String? = null,
            @SerializedName("fullName") val fullName: String? = null,
            @SerializedName("gender") val gender: String? = null,
            @SerializedName("lastName") val lastName: String? = null,
            @SerializedName("profilePic") val profilePic: Any? = null,
            @SerializedName("relation") val relation: String? = null,
            @SerializedName("state") val state: Any? = null,
            @SerializedName("userId") val userId: Int? = null,
            @SerializedName("userName") val userName: String? = null
        )
    }
}