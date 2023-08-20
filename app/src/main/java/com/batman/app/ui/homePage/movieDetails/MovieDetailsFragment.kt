package com.batman.app.ui.homePage.movieDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.batman.app.R
import com.batman.app.databinding.FragmentMovieDetailBinding
import com.batman.app.di.DaggerAppComponent
import com.batman.app.ui.homePage.movieDetails.viewmodel.MovieDetailViewModel
import com.batman.app.ui.homePage.movieDetails.viewmodel.MovieDetailsViewModelImpl
import com.core.base.ParentFragment
import com.core.dto.batman.BatmanDetail
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import javax.inject.Inject


class MovieDetailsFragment : ParentFragment<MovieDetailViewModel, FragmentMovieDetailBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.requestBatmanDetail(apiKey = "3e974fca", i = args.imdbId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBatmanDetailResponse().observe(viewLifecycleOwner) {
            val details = BatmanDetail(
                poster = it.poster,
                title = it.title,
                released = it.released,
                genre = it.genre,
                runtime = it.runtime,
                director = it.director,
                actors = it.actors,
                language = it.language,
                country = it.country,
                awards = it.awards,
                ratings = it.ratings[1].source + " " + it.ratings[1].value
            )
            dataBinding.progressBar.visibility = View.GONE
            dataBinding.movieDetails = details
        }
    }


    override fun getViewModelClass(): Class<MovieDetailViewModel> = MovieDetailViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailsViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_movie_detail

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }


}