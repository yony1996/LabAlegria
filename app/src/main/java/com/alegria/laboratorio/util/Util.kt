package com.alegria.laboratorio.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getDateFormatter(value: String, format: String = "dd/MM/yyyy"): String {
    return try {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val newFormatter = SimpleDateFormat(format, Locale.getDefault())
        val dateToday = df.parse(value) ?: Date()
        newFormatter.format(dateToday)
    } catch (e: ParseException) {
        e.printStackTrace()
        "-"
    }
}


