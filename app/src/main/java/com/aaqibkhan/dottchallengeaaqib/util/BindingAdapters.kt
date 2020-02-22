package com.aaqibkhan.dottchallengeaaqib.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        return
    }
    Picasso.get().load(imageUrl).into(imageView)
}