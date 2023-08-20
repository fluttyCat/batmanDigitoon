package com.core.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton

class ButtonCustom : AppCompatButton {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setEnabled(enabled: Boolean) {
        alpha = when {
            enabled -> 1.0f
            else -> 0.5f
        }
        super.setEnabled(enabled)
    }

    fun startDrawable(@DrawableRes resId  : Int){
        this.setCompoundDrawablesWithIntrinsicBounds(resId, 0,0,0)
    }
}