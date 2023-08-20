package com.batman.app.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batman.app.R

/* parisa;
coding and smoking!!
* */

class FooterAdapterVertical(val retry: () -> Unit) :
    LoadStateAdapter<FooterAdapterVertical.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.vertical_progress_bar)
        val retryButton: Button = itemView.findViewById(R.id.vertical_retry_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vertical_footer_item, parent, false)
        val holder = ViewHolder(view)
        holder.retryButton.setOnClickListener {
            retry()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.retryButton.isVisible = loadState is LoadState.Error
    }

}