package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.example.androidhomeapplication.data.repository.ImageType
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.*

@Parcelize
data class ConfigurationResponse(
    @Json(name = "images")
    val images: Images,

    @Json(name = "change_keys")
    val changeKeys: List<String>
) : Parcelable

@Parcelize
data class Images(
    @Json(name = "base_url")
    val baseUrl: String,

    @Json(name = "secure_base_url")
    val secureBaseUrl: String,

    @Json(name = "backdrop_sizes")
    val backdropSizes: List<String>,

    @Json(name = "logo_sizes")
    val logoSizes: List<String>,

    @Json(name = "poster_sizes")
    val posterSizes: List<String>,

    @Json(name = "profile_sizes")
    val profileSizes: List<String>,

    @Json(name = "still_sizes")
    val stillSizes: List<String>
) : Parcelable

fun ConfigurationResponse.getImageUrlByType(imageType: ImageType): String {
    val secureBaseURL = this.images.secureBaseUrl
    val imageSize = when (imageType) {
        ImageType.POSTER -> this.images.posterSizes.last()
        ImageType.BACKDROP -> this.images.backdropSizes.last()
        ImageType.PROFILE -> this.images.profileSizes.last()
    }
    return secureBaseURL + imageSize
}