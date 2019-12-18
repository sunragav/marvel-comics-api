package com.sunragav.indiecampers.feature_home.ui.recyclerview.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.sunragav.indiecampers.feature_home.databinding.ItemViewBinding
import com.sunragav.indiecampers.feature_home.ui.models.ComicsDataBindingModel
import com.sunragav.indiecampers.feature_home.ui.models.ComicsUIModel

class ComicsViewHolder(private val binding: ItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(comics: ComicsUIModel) {
        if (binding.comicsUiModelObserver == null)
            binding.comicsUiModelObserver = ComicsDataBindingModel(comics)
        else
            binding.comicsUiModelObserver!!.comics = comics
    }
}
