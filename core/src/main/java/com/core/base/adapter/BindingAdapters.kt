package com.core.base.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class BindingAdapters {
    companion object {
        @BindingAdapter("app:changeVisibility")
        @JvmStatic
        fun changeVisibility(view: View, value: Boolean) {
            view.visibility = if (value) View.VISIBLE else View.GONE
        }

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun setImageUrl(imageView: AppCompatImageView, imageUrl: String?) {
            Picasso.get()
                .load(imageUrl)
                .into(imageView)
        }

        @BindingAdapter("CircularImageUrl")
        @JvmStatic
        fun imageUrl(imageView: de.hdodenhof.circleimageview.CircleImageView, imageUrl: String?) {
            Picasso.get()
                .load("https://test$imageUrl")
                .into(imageView)
        }


    }
}

/*
*  val context = imageView.context
            val options: RequestOptions =
                RequestOptions().placeholder(R.drawable.ic_business_donate_v1)
                    .error(R.drawable.ic_launcher_background)
            Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(imageView)*/