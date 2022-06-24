package dang.lukas.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosResponseEntity(
    @Json(name = "hits") val photoEntities: List<PhotoEntity>
)
