package com.batman.app.ui.homePage.moviesPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.batman.app.R
import com.batman.app.databinding.FragmentMoviesBinding
import com.batman.app.di.DaggerAppComponent
import com.batman.app.ui.homePage.adapter.MoviesAdapter
import com.batman.app.ui.homePage.moviesPage.viewModel.MoviesViewModel
import com.batman.app.ui.homePage.moviesPage.viewModel.MoviesViewModelImpl
import com.batman.app.util.FooterAdapterVertical
import com.core.base.ParentFragment
import com.core.dto.batman.Batman
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class MoviesFragment : ParentFragment<MoviesViewModel, FragmentMoviesBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository


    private val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter {}
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initArticleRecycler()
        subscribeArticleItems()

    }

    private fun initArticleRecycler() {
        dataBinding.articleRv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter =
                moviesAdapter.withLoadStateFooter(FooterAdapterVertical { moviesAdapter.retry() })
        }
    }

    private fun subscribeArticleItems() {
        lifecycleScope.launch {
            viewModel.getMovieListData().collectLatest { bestPagingData ->
                val moviesData = bestPagingData.map {
                    Batman(
                        title = it.title,
                        year = it.year,
                        imdbID = it.imdbID!!,
                        type = it.type,
                        poster = it.poster,
                    )
                }

                moviesAdapter.submitData(moviesData)
                moviesAdapter.notifyDataSetChanged()
            }
        }
        moviesAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    dataBinding.progressBar.visibility = View.INVISIBLE
                    dataBinding.articleRv.visibility = View.VISIBLE
                }

                is LoadState.Loading -> {

                    dataBinding.articleRv.visibility = View.INVISIBLE
                    dataBinding.progressBar.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error

                    dataBinding.progressBar.visibility = View.INVISIBLE

                    Toast.makeText(
                        requireActivity(),
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                }
            }
        }
    }

    override fun getViewModelClass(): Class<MoviesViewModel> = MoviesViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MoviesViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_movies

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }

}