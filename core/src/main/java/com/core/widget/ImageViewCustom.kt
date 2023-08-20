package com.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.core.R


class ImageViewCustom : AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.ImageViewCustom)
        val resource = typed.getInt(R.styleable.ImageViewCustom_imageResource, -1)
        if (resource > -1) {
            setResource(resource)
        }

    }

    override fun setEnabled(enabled: Boolean) {
        alpha = when {
            enabled -> 1.0f
            else -> 0.5f
        }
        super.setEnabled(enabled)
    }

    fun setResource(resource: Int) {
        //add placeholder in case resource is null
        this.setImageResource(resource)
    }
}