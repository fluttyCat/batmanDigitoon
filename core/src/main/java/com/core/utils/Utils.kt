package com.core.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class Utils {

    companion object {

        fun dpToPx(context: Context, dp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }

        fun pxToDp(context: Context, px: Int): Int {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            return Math.round((px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toDouble()).toInt()
        }

        fun convertMillisecondToTime(value: Long): String {
            return if (value == 0L) {
                "00:00:00"
            } else {
                val time = TimeUnit.MILLISECONDS.toSeconds(value)
                val min  = time / 60
                val sec  = time % 60
                val seconds100      = ((value - TimeUnit.SECONDS.toMillis(time)).toFloat() / 10).roundToInt()
                String.format("%s:%s:%s",
                    if (min < 10) {
                        "0$min"
                    } else {
                        "$min"
                    },
                    if (sec < 10) {
                        "0$sec"
                    } else {
                        "$sec"
                    },
                    if (seconds100 < 10) {
                        "0$seconds100"
                    } else {
                        "$seconds100"
                    }
                )
            }
        }
    }
}