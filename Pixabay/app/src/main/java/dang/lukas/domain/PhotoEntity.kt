package dang.lukas.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PhotoEntity(
    @Json(name = "id") val id: Long,
    @Json(name = "tags") val tags: String,
    @Json(name = "webformatURL") val url: String,
    @Json(name = "user") val user: String,
    @Json(name = "views") val views: Int,
    @Json(name = "downloads") val downloads: Int,
    @Json(name = "likes") val likes: Int,
    @Json(name = "comments") val comments: Int,
    @Json(name = "userImageURL") val profilePictureUrl: String,
    @Json(name = "pageURL") val pageUrl: String,
    @Json(name = "user_id") val userId: Long,
)
