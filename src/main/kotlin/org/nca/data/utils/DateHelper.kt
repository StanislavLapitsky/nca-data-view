package org.nca.data.utils

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_DATE_FORMAT = "yyyy.MM.dd"
const val DEFAULT_DATE_TIME_FORMAT = "yyyy.MM.dd HH:mm:ss"


object DateHelper {
    @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.US)
        }
    }
    @JvmField val DF_SIMPLE_DATE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US)
        }
    }
}

fun dateNow(): String = Date().asDateString()
fun dateTimeNow(): String = Date().asDateString()

fun timestamp(): Long = System.currentTimeMillis()

fun dateParse(s: String): Date = DateHelper.DF_SIMPLE_DATE_FORMAT.get().parse(s, ParsePosition(0))
fun dateTimeParse(s: String): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(s, ParsePosition(0))

fun dateParse(s: String, format: String): Date = SimpleDateFormat(format).parse(s, ParsePosition(0))

fun Date.asDateString(format: DateFormat): String = format.format(this)

fun Date.asDateString(format: String): String = asDateString(SimpleDateFormat(format))

fun Date.asDateString(): String = DateHelper.DF_SIMPLE_DATE_FORMAT.get().format(this)
fun Date.asDateTimeString(): String = DateHelper.DF_SIMPLE_FORMAT.get().format(this)

fun Long.asDateString(): String = Date(this).asDateString()
