package com.sunragav.indiecampers.feature_home.ui.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.feature_home.databinding.ItemViewBinding
import com.sunragav.indiecampers.feature_home.ui.mapper.ComicsUIEntityMapper
import com.sunragav.indiecampers.feature_home.ui.recyclerview.viewholders.ComicsViewHolder
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity


class ComicsPagedListAdapter(
    private val comicsUIEntityMapper: ComicsUIEntityMapper
) :
    PagedListAdapter<ComicsEntity, ComicsViewHolder>(COMICS_COMPARATOR) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemComicsBinding = DataBindingUtil.inflate<ItemViewBinding>(
            layoutInflater,
            R.layout.item_view,
            parent,
            false
        )
        return ComicsViewHolder(itemComicsBinding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, comicsUIEntityMapper) }
    }

    companion object {
        private val COMICS_COMPARATOR = object : DiffUtil.ItemCallback<ComicsEntity>() {
            override fun areItemsTheSame(oldItem: ComicsEntity, newItem: ComicsEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ComicsEntity, newItem: ComicsEntity): Boolean =
                oldItem == newItem
        }
    }
}
