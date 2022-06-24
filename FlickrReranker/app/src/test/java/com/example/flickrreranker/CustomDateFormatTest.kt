package com.example.flickrreranker

import com.example.flickrreranker.app.ui.helper.CustomDateFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomDateFormatTest {

    @Test
    fun testConversionFromUnixMillisToDateString() {
        assertEquals(CustomDateFormat.unixMillisToDateString(1636156885), "06.11.2021")
    }
}