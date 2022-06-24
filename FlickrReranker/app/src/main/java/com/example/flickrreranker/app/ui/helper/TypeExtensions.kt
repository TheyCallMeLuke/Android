package com.example.flickrreranker.app.ui.helper

import android.text.Editable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private const val DATE_PATTERN = "dd.MM.yyyy"

fun Editable?.toInt(): Int {
    return this.toString().toInt()
}

fun Editable?.toDouble(): Double {
    return this.toString().toDouble()
}

fun Long.toDateString(): String {
    return SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(this)
}

fun String.toUnix(): Long {
    val l = LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_PATTERN))
    return l.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}