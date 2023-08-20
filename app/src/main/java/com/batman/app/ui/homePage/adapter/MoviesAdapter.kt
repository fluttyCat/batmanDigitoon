package com.batman.app.ui.homePage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.batman.app.databinding.ItemMoviesPreviewBinding
import com.core.base.adapter.BaseHolder
import com.core.dto.batman.Batman

class MoviesAdapter(private val itemClickCallback: (Batman) -> Unit) :
    PagingDataAdapter<Batman, BaseHolder<Batman>>(object :
        DiffUtil.ItemCallback<Batman>() {

        override fun areItemsTheSame(
            oldItem: Batman,
            newItem: Batman
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: Batman,
            newItem: Batman
        ): Boolean {
            return oldItem == newItem
        }

    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Batman> {

        return ArticleViewHolder(
            ItemMoviesPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: BaseHolder<Batman>, position: Int) {
        holder.bind(getItem(position)!!, position)
    }


    inner class ArticleViewHolder(
        private val binding: ItemMoviesPreviewBinding
    ) : BaseHolder<Batman>(binding) {

        override fun bind(value: Batman, position: Int) {
            binding.movieItem = value

            binding.root.setOnClickListener {
                itemClickCallback(value)
            }
        }
    }
}
