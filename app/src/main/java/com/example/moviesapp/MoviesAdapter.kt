package com.example.moviesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviesapp.databinding.MovieItemCardBinding
import com.example.networksdk.data.Movie
import com.example.networksdk.services.MovieService

class MoviesAdapter(private val context: Context, private val listener: MoviesListener) :
        ListAdapter<Movie, MovieViewHolder>(MoviesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MovieItemCardBinding = DataBindingUtil.inflate(inflater, R.layout.movie_item_card,
                parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position), context)
        if (position == currentList.size - 1) {
            listener.loadMoreData()
        }
    }
}

class MovieViewHolder(private val binding: MovieItemCardBinding) : RecyclerView.ViewHolder(binding
        .root) {
    fun bind(data: Movie, context: Context) {
        binding.tvName.text = data.title
        binding.ratingBar.rating = (data.voteAverage / 2).toFloat()
        Glide.with(context)
                .load(data.posterPath?.let { MovieService.getImageUrl(it) })
                .apply(RequestOptions().optionalCenterCrop())
                .into(binding.ivPoster)
    }
}

class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
