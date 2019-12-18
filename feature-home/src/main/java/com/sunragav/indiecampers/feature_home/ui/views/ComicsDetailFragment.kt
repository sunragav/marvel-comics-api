package com.sunragav.indiecampers.feature_home.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.feature_home.databinding.FragmentDetailBinding
import com.sunragav.indiecampers.feature_home.ui.models.ComicsDataBindingModel
import com.sunragav.indiecampers.feature_home.ui.models.ComicsUIModel
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM


class ComicsDetailFragment(private val currentComics: ComicsUIModel) : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: HomeVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        activity?.let { viewModel = ViewModelProviders.of(it).get(HomeVM::class.java) }

        binding.comicsUiModelObserver = ComicsDataBindingModel(currentComics)
        return binding.root
    }


}
