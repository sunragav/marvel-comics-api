package com.sunragav.indiecampers.feature_home.ui.bindings

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sunragav.indiecampers.feature_home.R

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView?, field: ObservableField<String>?) {

        val url = field?.get()
        if (!url.isNullOrBlank() && view != null) {
            Glide.with(view.context)
                .load(url).apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .dontAnimate()
                        .dontTransform()
                        .encodeFormat(Bitmap.CompressFormat.PNG)
                        .override(700)
                        .format(DecodeFormat.PREFER_RGB_565)
                        .sizeMultiplier(0.5f)
                        .fitCenter()
                        .placeholder(R.drawable.marvel_thumbnail)
                )
                .into(view)
        }

}
