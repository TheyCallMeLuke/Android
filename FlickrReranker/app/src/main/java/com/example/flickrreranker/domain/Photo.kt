package com.example.flickrreranker.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPhotoResponse(
    @Json(name = "photos") val photosWrapper: PhotosWrapper,
    @Json(name = "stat") val stat: String
)

@JsonClass(generateAdapter = true)
data class PhotosWrapper(
    @Json(name = "photo") val photoData: List<Photo>,
)

/**
 * This is the main domain class which is used throughout the program.
 */
@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "id") val id: String,
    @Json(name = "ownername") val author: String,
    @Json(name = "dateupload") val date: Long,
    @Json(name = "views") val viewCount: Int,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "url_l") val url_l: String?,
    @Json(name = "url_o") val url_o: String?,
    @Json(name = "url_c") val url_c: String?,
    @Json(name = "url_z") val url_z: String?,
    @Json(name = "url_n") val url_n: String?,
    @Json(name = "url_m") val url_m: String?,
    @Json(name = "url_q") val url_q: String?,
    @Json(name = "url_s") val url_s: String?,
    @Json(name = "url_t") val url_t: String?,
    @Json(name = "url_sq") val url_sq: String?,
) {
    /**
     * The score is calculated dynamically.
     */
    var score: Double = 0.0

    /**
     * The corresponding image URL of the photo.
     */
    var imgSrcUrl = url()

    /**
     * A function that returns an image URL by using a kind of fallback mechanism where is type of URL
     * does not exist, it fallbacks to the subsequent URL. Each URL represents an image with a
     * particular dimension. The problem is that not every photo has a URL to its corresponding image
     * in every dimension, so we use this fallback mechanism to find any image URL.
     */
    private fun url(): String? {
        return url_l ?: url_o ?: url_c ?: url_z ?: url_n ?: url_m
        ?: url_q ?: url_s ?: url_t ?: url_sq
    }
}