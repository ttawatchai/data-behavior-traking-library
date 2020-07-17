package com.ttawatchai.databehaviortracking.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtility {

    companion object {
        const val DateTimeDatabasePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        const val DateDatabasePattern = "dd/MM/yyyy"
        const val DatabasePattern = "dd-MM-yyyy HH:mm:ss"

//        const val DateTimeDatabasePattern = "yyyy-MM-dd HH:mm:ss.SSS"

        const val DateTimeDisplayPattern = "dd/MM/yyyy hh:mm a"
        const val DateDisplayPattern = "dd-MM-yyyy"
        const val TimeDisplayPattern = "hh:mm a"
        const val Time24Pattern = "kk:mm:ss"

        fun current(): LocalDateTime = LocalDateTime.now()

        fun currentTime(): Date = Calendar.getInstance().time

        fun dateNow() = currentDate(DateTimeDatabasePattern)


        fun currentDate(pattern: String) = SimpleDateFormat(pattern).format(currentTime())

        fun timeNow(pattern: String): String {                                 // 24 hr
            val dateFormat = SimpleDateFormat(pattern).format(currentTime())
            return dateFormat.toString()
        }

        fun SystemTime() = System.currentTimeMillis().toString()

        fun parseDatabaseDate(date: String) =
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DateTimeDatabasePattern))

        fun convertToDisplayFormat(dateTime: String): String {
            val databasePattern = DateTimeFormatter.ofPattern(DateTimeDatabasePattern)
            val displayPattern = DateTimeFormatter.ofPattern(DateTimeDisplayPattern)
            return LocalDateTime.parse(dateTime, databasePattern).format(displayPattern)
        }

        fun convertToDateDisplayFormat(localDateTime: LocalDateTime): String {
            val displayPattern = DateTimeFormatter.ofPattern(DateDisplayPattern)
            return localDateTime.format(displayPattern)
        }

        fun convertToDateDatabaseFormat(localDateTime: LocalDateTime): String {
            val databasePattern = DateTimeFormatter.ofPattern(DateDatabasePattern)
            return localDateTime.format(databasePattern)
        }

        fun convertToDisplayFormat(dateTime: LocalDateTime): String {
            val displayPattern = DateTimeFormatter.ofPattern(DateTimeDisplayPattern)
            return dateTime.format(displayPattern)
        }

        fun convertToDatabaseFormat(dateTime: LocalDateTime): String {
            val databasePattern = DateTimeFormatter.ofPattern(DateTimeDatabasePattern)
            return dateTime.format(databasePattern)
        }

        fun convertStringToDate(sDate: String): Date {
            val format = SimpleDateFormat(DateTimeDatabasePattern, Locale.US)
            var date = Date()
            try {
                date = format.parse(sDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }


    }
}