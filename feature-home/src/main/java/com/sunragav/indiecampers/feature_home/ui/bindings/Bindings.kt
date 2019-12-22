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
    setImageUrl(view, field, 220, 300)
}


@BindingAdapter("bigImageUrl")
fun setBigImageUrl(view: ImageView?, field: ObservableField<String>?) {
    if (view?.context?.resources?.getBoolean(R.bool.isTablet)==true)
        setImageUrl(view, field, 1500, 1500)
    else
        setImageUrl(view, field, 1100, 1500)

}

fun setImageUrl(view: ImageView?, field: ObservableField<String>?, width: Int, height: Int) {

    val url = field?.get()
    if (!url.isNullOrBlank() && view != null) {
        Glide.with(view.context)
            .load(url).apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .dontTransform()
                    .encodeFormat(Bitmap.CompressFormat.PNG)
                    .override(width, height)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .sizeMultiplier(0.5f)
                    .placeholder(R.drawable.marvel_thumbnail)
            )
            .into(view)
    }

}
