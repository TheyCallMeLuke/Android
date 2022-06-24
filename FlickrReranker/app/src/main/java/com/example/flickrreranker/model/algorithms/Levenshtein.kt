package com.example.flickrreranker.model.algorithms

import kotlin.math.min

/**
 * The levenshtein distance algorithm calculates the minimum number of single-character edits
 * (insertions, deletions or substitutions) required to change on string into the other. Here, it is
 * used to calculate the distance between two author names.
 *
 * See: https://en.wikipedia.org/wiki/Levenshtein_distance
 */
object Levenshtein {

    private lateinit var lhs: String
    private lateinit var rhs: String

    fun calc(lhs: String, rhs: String): Int {
        Levenshtein.lhs = lhs
        Levenshtein.rhs = rhs
        return calc(0, 0)
    }

    private fun calc(i: Int, j: Int): Int {
        if (i >= lhs.length) return rhs.length - j
        if (j >= rhs.length) return lhs.length - i
        var distanceAfterSkip = calc(i + 1, j + 1)
        if (lhs[i] != rhs[j]) {
            distanceAfterSkip++
        }
        val distanceAfterDelete = 1 + calc(i + 1, j)
        val distanceAfterAdd = 1 + calc(i, j + 1)
        return min(distanceAfterSkip, min(distanceAfterDelete, distanceAfterAdd))
    }
}