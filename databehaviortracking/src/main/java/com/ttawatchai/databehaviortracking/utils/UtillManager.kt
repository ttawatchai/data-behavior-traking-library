package com.ttawatchai.databehaviortracking.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.math.*

class UtillManager {
    companion object {

        fun convertTo2Digit(value: Double): Double {
            val number3digits: Double = String.format("%.3f", value).toDouble()
            return String.format("%.2f", abs(number3digits)).toDouble()
        }

        fun doubleIsNotNull(value: Double?): Double {
            return value ?: 0.00
        }

        fun stringIsNotNull(value: String?): String {
            return value ?: ""
        }

        @SuppressLint("HardwareIds")
        fun requestDeviceId(context: Context): String {
            return Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }


        fun mps_to_kmph(mps: Double): Double {
            return convertTo2Digit(3.6 * mps)
        }

        fun hideKeyboard(activity: Activity, view: View) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
        }

        fun isLocationEnabled(mContext: Context): Boolean {
            val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        fun distance(
            prevLat: Double,
            prevLon: Double,
            lastLat: Double,
            lastLon: Double
        ): Double {
            val dlat = deg2rad(lastLat - prevLat)
            val dlon = deg2rad(lastLon - prevLon)
            val a =
                (sin(dlat / 2)).pow(2) + cos(deg2rad(prevLat)) * cos(deg2rad(lastLat)) * (sin(dlon / 2)).pow(
                    2
                )
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (c * 1000 * 6371) //6371 is radius of Earth
        }


        fun underlineText(address: String): SpannableString {
            val content = SpannableString(address)
            content.setSpan(
                UnderlineSpan(),
                0,
                address.length,
                0
            )
            return content
        }


        fun stopSound(mp: MediaPlayer): MediaPlayer? {
            try {
                mp.stop()
                mp.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }



        fun spannableString(text: String, start: Int, end: Int, size: Float): SpannableString {
            val spannable = SpannableString(text)
            spannable.setSpan(
                RelativeSizeSpan(size),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            ) // set size
            return spannable
        }

        fun bitmapToFile(bitmap: Bitmap, context: Context): File {
            // Get the context wrapper
            val wrapper = ContextWrapper(context)

            // Initialize a new file instance to save bitmap object
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            file = File(file, "signature.jpg")

            try {
                // Compress the bitmap and save in jpg format
                val stream: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                stream.flush()
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        }
    }
}
