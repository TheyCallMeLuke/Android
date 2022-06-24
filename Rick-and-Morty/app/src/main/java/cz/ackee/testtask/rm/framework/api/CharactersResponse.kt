package cz.ackee.testtask.rm.framework.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersResponse(
    @Json(name = "results") val characterResponseEntities: List<CharacterResponseEntity>
)
