package com.sunragav.indiecampers.feature_home.ui.models

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField

class ComicsDataBindingModel(var comics: ComicsUIModel) : BaseObservable() {

    val id: ObservableField<String> = ObservableField(comics.id)
    val title: ObservableField<String> = ObservableField(comics.title)
    val description: ObservableField<String> = ObservableField(comics.description)
    val imageUrl: ObservableField<String> = ObservableField(comics.imageUrls[0])

    fun onClick(view: View) {

    }
}

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}
