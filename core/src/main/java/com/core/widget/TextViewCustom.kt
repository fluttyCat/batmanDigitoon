package com.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.AttributeSet
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatTextView
import com.core.R
import com.core.utils.Utils
import java.util.*

class TextViewCustom : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.TextViewCustom)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDateTime(date: Date?) {
        date?.let {
            text = generateDate(it)
        }
    }


    fun setTiimer(time: Long?) {
        time?.let {
            var stringBuilder = StringBuilder()
            text = stringBuilder.append(" ").append(Utils.convertMillisecondToTime(it))
                .append(" ثانیه")
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun generateDate(date: Date): String {
        return DateFormat.format("dd.MM.yyyy - HH:mm", date) as String
    }

}