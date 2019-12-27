package com.sunragav.indiecampers.feature_home.ui.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.sunragav.indiecampers.feature_home.R
import com.sunragav.indiecampers.feature_home.databinding.FragmentComicsListFeatureBinding
import com.sunragav.indiecampers.feature_home.ui.mapper.ComicsUIEntityMapper
import com.sunragav.indiecampers.feature_home.ui.recyclerview.adapters.ComicsPagedListAdapter
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.RepositoryState
import com.sunragav.indiecampers.home.domain.entities.RepositoryStateRelay
import com.sunragav.indiecampers.home.presentation.factory.ComicsViewModelFactory
import com.sunragav.indiecampers.home.presentation.viewmodels.HomeVM
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class ComicsListFeatureFragment : Fragment() {
    private lateinit var binding: FragmentComicsListFeatureBinding
    private lateinit var viewModel: HomeVM
    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory

    @Inject
    lateinit var disposable: CompositeDisposable

    @Inject
    lateinit var repositoryStateRelay: RepositoryStateRelay


    private lateinit var comicsListAdapter: ComicsPagedListAdapter


    private var query: String = ""


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
            R.layout.fragment_comics_list_feature,
            container,
            false
        )
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.rvComicsList.addItemDecoration(decoration)
        binding.rvComicsList.layoutManager = GridLayoutManager(activity, 1)
        binding.rvComicsList.setHasFixedSize(true)


        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(HomeVM::class.java)
        }

        query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: (viewModel.lastSearchQuery()?:DEFAULT_QUERY)

        binding.viewModel = viewModel


        initAdapter(binding)
        initListeners()
        initSearch()
        return binding.root
    }

    private val pagedListLiveDataObserver = Observer<PagedList<ComicsEntity>> { pagedList ->
        Log.d(
            "ComicsListFeatureActivityFragment",
            "Current paged list size: ${pagedList?.size} query= $query"
        )
        showEmptyList(pagedList?.size == 0)
        comicsListAdapter.submitList(pagedList)
        comicsListAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        viewModel.comicsListSource.observe(this, pagedListLiveDataObserver)
        val subscription =
            repositoryStateRelay.relay.subscribe {
                when (it) {
                    RepositoryState.LOADING -> viewModel.isLoading.set(true)
                    RepositoryState.LOADED -> {
                        viewModel.isLoading.set(false)
                    }
                    RepositoryState.DISCONNECTED -> {
                        Toast.makeText(
                            activity,
                            R.string.network_lost,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    RepositoryState.CONNECTED -> {
                        if (viewModel.isLoading.get() == true) {
                            viewModel.search(query)
                        }
                    }
                    RepositoryState.ERROR -> {
                        viewModel.isLoading.set(false)
                        Toast.makeText(
                            activity,
                            R.string.network_error,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    RepositoryState.EMPTY -> {
                        viewModel.search(query)
                    }
                }
            }
        subscription?.let { disposable.add(it) }
    }

    override fun onDetach() {
        super.onDetach()
        disposable.dispose()

    }


    override fun onDestroyView() {
        binding.rvComicsList.addOnAttachStateChangeListener(object :
            View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) { // no-op
            }

            override fun onViewDetachedFromWindow(v: View) {
                binding.rvComicsList.adapter = null
            }
        })
        super.onDestroyView()
    }

    private fun initAdapter(binding: FragmentComicsListFeatureBinding) {
        comicsListAdapter = ComicsPagedListAdapter(ComicsUIEntityMapper())
        binding.rvComicsList.adapter = comicsListAdapter
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.rvComicsList.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.rvComicsList.visibility = View.VISIBLE
            repositoryStateRelay.relay.accept(RepositoryState.DB_LOADED)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastSearchQuery())
    }


    private fun updateComicsListFromInput() {
        binding.searchComics.text.trim().let {
            if (it.isNotEmpty()) {
                // binding.rvComicsList.scrollToPosition(0)
                query = it.toString()
                viewModel.search(query)
                comicsListAdapter.submitList(null)
                comicsListAdapter.notifyDataSetChanged()
                binding.rvComicsList.recycledViewPool.clear()
            }
        }
    }

    private fun initSearch() {
        binding.searchComics.setText(query)

        binding.searchComics.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateComicsListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchComics.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateComicsListFromInput()
                true
            } else {
                false
            }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Avengers"
    }
}
