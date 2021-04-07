package com.example.androidhomeapplication.data.remote.response

import com.example.androidhomeapplication.data.repository.ImageType
import kotlinx.serialization.*

@Serializable
data class ConfigurationResponse(
    @SerialName("images")
    val images: Images,

    @SerialName("change_keys")
    val changeKeys: List<String>
)

@Serializable
data class Images(
    @SerialName("base_url")
    val baseUrl: String,

    @SerialName("secure_base_url")
    val secureBaseUrl: String,

    @SerialName("backdrop_sizes")
    val backdropSizes: List<String>,

    @SerialName("logo_sizes")
    val logoSizes: List<String>,

    @SerialName("poster_sizes")
    val posterSizes: List<String>,

    @SerialName("profile_sizes")
    val profileSizes: List<String>,

    @SerialName("still_sizes")
    val stillSizes: List<String>
)

fun ConfigurationResponse.getImageUrlByType(imageType: ImageType): String {
    val secureBaseURL = this.images.secureBaseUrl
    val imageSize = when (imageType) {
        ImageType.POSTER -> this.images.posterSizes.last()
        ImageType.BACKDROP -> this.images.backdropSizes.last()
        ImageType.PROFILE -> this.images.profileSizes.last()
    }
    return secureBaseURL + imageSize
}