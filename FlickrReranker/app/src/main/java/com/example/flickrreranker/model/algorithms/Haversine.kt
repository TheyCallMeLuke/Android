package com.example.flickrreranker.model.algorithms

import kotlin.math.*

/**
 * The haversine algorithm calculates the great-circle distance (distance along a circle) between
 * two points on a sphere given their longitudes and latitudes. Here, it is used to calculate
 * the distance between the geo locations of two photos.
 *
 * See: https://en.wikipedia.org/wiki/Haversine_formula
 */
object Haversine {

    private const val earthRadiusKm: Double = 6372.8

    fun calc(dstLat: Double, dstLon: Double, srcLat: Double, srcLon: Double): Double {
        val dLat = Math.toRadians(dstLat - srcLat)
        val dLon = Math.toRadians(dstLon - srcLon)
        val originLat = Math.toRadians(srcLat)
        val destinationLat = Math.toRadians(dstLat)

        val a =
            sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(
                destinationLat
            )
        val c = 2 * asin(sqrt(a))
        return earthRadiusKm * c
    }

}
