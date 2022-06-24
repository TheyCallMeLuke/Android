package com.example.flickrreranker

import com.example.flickrreranker.model.algorithms.Levenshtein
import org.junit.Assert.assertEquals
import org.junit.Test

class LevenshteinTest {

    @Test
    fun test() {
        assertEquals(Levenshtein.calc("abba", "cba"), 2)
        assertEquals(Levenshtein.calc("a", "a"), 0)
        assertEquals(Levenshtein.calc("a", ""), 1)
        assertEquals(Levenshtein.calc("", ""), 0)
        assertEquals(Levenshtein.calc("ER's Eyes - Our planet is beautiful.", ""), 0)
    }

}