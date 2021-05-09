package com.catasoft.autoclub.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.catasoft.autoclub.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception


object DataBindingAdapters {
    @BindingAdapter("bind:imageUri")
    @JvmStatic
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageDrawable(ResourcesCompat.getDrawable(view.context.resources, R.drawable.ic_placeholder_view_vector, null))
        } else {
            Picasso.get().load(imageUri).resize(400, 400).into(object: Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    view.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
    }

    @BindingAdapter("bind:imageUri")
    @JvmStatic
    fun setImageUri(view: ImageView, imageUri: Uri?) {
        Picasso.get().load(imageUri).resize(400, 400).into(object: Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                view.setImageBitmap(bitmap)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    @BindingAdapter("bind:imageUri")
    @JvmStatic
    fun setImageDrawable(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    @BindingAdapter("bind:imageUri")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }
}