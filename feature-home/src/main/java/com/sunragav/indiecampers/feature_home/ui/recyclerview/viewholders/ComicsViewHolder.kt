package com.sunragav.indiecampers.feature_home.ui.recyclerview.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.sunragav.indiecampers.feature_home.databinding.ItemViewBinding
import com.sunragav.indiecampers.feature_home.ui.bindings.ComicsDataBindingModel
import com.sunragav.indiecampers.feature_home.ui.mapper.ComicsUIEntityMapper
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM

class ComicsViewHolder( val binding: ItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        comicsEntity: ComicsEntity,
        viewModel: HomeVM,
        comicsUIEntityMapper: ComicsUIEntityMapper
    ) {
        if (binding.comicsUiModelObserver == null)
            binding.comicsUiModelObserver =
                ComicsDataBindingModel(
                    comicsUIEntityMapper.to(comicsEntity), viewModel, comicsUIEntityMapper
                )
        else
            binding.comicsUiModelObserver!!.comics = comicsUIEntityMapper.to(comicsEntity)
    }
}
