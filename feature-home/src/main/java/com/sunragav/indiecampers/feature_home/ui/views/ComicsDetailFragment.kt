package com.sunragav.indiecampers.feature_home.ui.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.feature_home.databinding.FragmentDetailBinding
import com.sunragav.indiecampers.feature_home.ui.bindings.ComicsDataBindingModel
import com.sunragav.indiecampers.feature_home.ui.mapper.ComicsUIEntityMapper
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.presentation.factory.ComicsViewModelFactory
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class ComicsDetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: HomeVM
    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory
    private val comicsUIEntityMapper = ComicsUIEntityMapper()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

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

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(HomeVM::class.java)
        }


        viewModel.currentComics.observe(this, Observer<ComicsEntity> {
            if (it.id != "default")
                binding.comicsUiModelObserver =
                    ComicsDataBindingModel(
                        comicsUIEntityMapper.to(it),
                        viewModel,
                        comicsUIEntityMapper
                    )

        })

        return binding.root
    }


}
