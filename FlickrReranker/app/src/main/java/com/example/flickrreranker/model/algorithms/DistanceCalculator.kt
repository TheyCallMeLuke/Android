package com.example.flickrreranker.model.algorithms

import com.example.flickrreranker.domain.Photo
import com.example.flickrreranker.model.PhotoViewModel
import kotlin.math.abs

private const val NUMBER_OF_SCORE_PARAMETERS = 4

/**
 * A calculator for calculating the distances of photo metadata values (author name, date uploaded, geo location, number of views) to the desired metadata values.
 *
 * Each distance of each type of metadata is calculated using a different algorithm.
 * E.g. calculating the distance between two author names is a equivalent to the problem of calculating the distance between two strings.
 * The algorithm to calculate this distance is called Levensthtein. 
 * The distance between two geo locations is calculated using the Haversine algorithm.
 * The distance between two dates or two view counts is calculated simply by their absolute difference.
 * 
 * Now we need to combine these individual distances in a way, so we can say, how close a photo's metadata is to our desired metadata.
 * This is accomplished by first normalizing the values by dividing the distance of each metadata type of each photo by the maximum distance of that particular metadata type of a particular photo.
 * The result is that each distance of each metadata has the value between 0 and 1.
 * In other words, for each photo, the author name distance has a value between 0 and 1, the uploaded date has a value between 0 and 1, etc.
 * At last, we sum all distances up. The result can be greater than 1, but we want it normalized. So we divide this result by the number of metadata parameters (which is currently 4).
 * At the end, we have one single value of each photo, which has a score between 0 and 1. The photo with a score closest to 1 is the one whose metadata is closest to the desired metadata.
 * Conversely, a photo with a score closest to 0 is the one whose metadata is the farthest from the desired metadata.
 */
class DistanceCalculator(private val viewModel: PhotoViewModel) {

    fun recalculateScoresWithViewCountDistances(photos: MutableList<Photo>) {
        if (viewModel.viewCountEnabled) {
            val maxDistance = photos.getMaxViewCountDist(viewModel.viewCount!!)
            photos.forEach {
                val distance = abs(viewModel.viewCount!! - it.viewCount)
                it.score += distance / maxDistance / NUMBER_OF_SCORE_PARAMETERS
            }
        }
    }

    fun calculateScoreFromAuthor(photos: MutableList<Photo>) {
        if (viewModel.authorEnabled) {
            val maxDistance = photos.getMaxAuthorDist(viewModel.author!!)
            photos.forEach {
                val dist = Levenshtein.calc(it.author, viewModel.author!!)
                it.score += dist / maxDistance / NUMBER_OF_SCORE_PARAMETERS
            }
        }
    }

    fun calculateScoreFromDate(photos: MutableList<Photo>) {
        if (viewModel.dateEnabled) {
            val maxDistance = photos.getMaxDateDist(viewModel.date!!)
            photos.forEach {
                val distance = abs(viewModel.date!! - it.date)
                it.score += distance / maxDistance / NUMBER_OF_SCORE_PARAMETERS
            }
        }
    }

    fun calculateScoreFromLocation(photos: MutableList<Photo>) {
        if (viewModel.geoEnabled) {
            val maxDistance = photos.getMaxGeoDistance(viewModel.latitude!!, viewModel.latitude!!)
            photos.forEach {
                val distance = Haversine.calc(
                    it.latitude,
                    it.longitude,
                    viewModel.latitude!!,
                    viewModel.longitude!!
                )
                it.score += distance / maxDistance / NUMBER_OF_SCORE_PARAMETERS
            }
        }
    }
}

private fun MutableList<Photo>.getMaxViewCountDist(viewCount: Int): Int {
    val worstPhoto = this.maxByOrNull { abs(viewCount - it.viewCount) } ?: return Int.MAX_VALUE
    return abs(worstPhoto.viewCount - viewCount)
}

private fun MutableList<Photo>.getMaxGeoDistance(
    latitude: Double,
    longitude: Double
): Double {
    val worstPhoto =
        this.maxByOrNull { Haversine.calc(it.latitude, it.longitude, latitude, longitude) }
            ?: return Double.MAX_VALUE
    return Haversine.calc(worstPhoto.latitude, worstPhoto.longitude, latitude, longitude)
}

private fun MutableList<Photo>.getMaxDateDist(targetDate: Long): Long {
    val worstPhoto = this.maxByOrNull { abs(targetDate - it.date) } ?: return Long.MAX_VALUE
    return abs(worstPhoto.date - targetDate)
}

private fun MutableList<Photo>.getMaxAuthorDist(targetAuthor: String): Int {
    val worstPhoto =
        this.maxByOrNull { Levenshtein.calc(it.author, targetAuthor) } ?: return Int.MAX_VALUE
    return Levenshtein.calc(worstPhoto.author, targetAuthor)
}
