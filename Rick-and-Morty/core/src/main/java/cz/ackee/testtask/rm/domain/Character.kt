package cz.ackee.testtask.rm.domain

import kotlin.properties.Delegates

data class Character(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
) {
    data class Location(val name: String)
    data class Origin(val name: String)

    /**
     * Is set later by checking the database.
     */
    var isSaved by Delegates.notNull<Boolean>()
}