package com.batman.app.ui.homePage.articleDetails

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batman.app.R
import com.batman.app.databinding.FragmentArticleDetailBinding
import com.batman.app.di.DaggerAppComponent
import com.batman.app.ui.homePage.moviesPage.viewModel.MoviesViewModel
import com.batman.app.ui.homePage.moviesPage.viewModel.MoviesViewModelImpl
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import javax.inject.Inject


class ArticlesDetailFragment : ParentFragment<MoviesViewModel, FragmentArticleDetailBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeArticlesData()
    }


    private fun subscribeArticlesData() {
        //Picasso.get().load(args.url).into(dataBinding.ivArticleDetailImage)
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

    override fun getResourceLayoutId(): Int = R.layout.fragment_article_detail

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }


}