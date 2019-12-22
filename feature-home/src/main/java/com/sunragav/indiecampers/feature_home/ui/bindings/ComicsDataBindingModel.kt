package com.sunragav.indiecampers.feature_home.ui.bindings

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.navigation.findNavController
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.feature_home.ui.mapper.ComicsUIEntityMapper
import com.sunragav.indiecampers.feature_home.ui.models.ComicsUIModel
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivity


class ComicsDataBindingModel(
    var comics: ComicsUIModel,
    private val comicsUIEntityMapper: ComicsUIEntityMapper
) : BaseObservable() {

    val id: ObservableField<String> = ObservableField(comics.id)
    val title: ObservableField<String> = ObservableField(comics.title)
    val description: ObservableField<String> = ObservableField(comics.description)
    val imageUrl: ObservableField<String> = ObservableField(comics.thumbNail)

    fun onClick(view: View) {
        (view.context as ComicsListFeatureActivity).viewModel.currentComics.postValue(comicsUIEntityMapper.from(comics))
        if (view.context.resources.getBoolean(R.bool.isTablet).not())
            view.findNavController()
                .navigate(R.id.action_comicsListFeatureActivityFragment_to_comicsDetailFragment)
    }
}


