package com.example.watchlist.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.watchlist.databinding.MovieItemBinding
import com.example.watchlist.db.Movie

class MovieAdapter(private val movieAdapterListener: MovieAdapterListener) : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie, movieAdapterListener)
        }
    }
}

interface MovieAdapterListener {

    fun onEdit(movie: Movie)

    fun onDelete(movie: Movie)
}

class MovieViewHolder(
    private val binding: MovieItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var movie: Movie

    fun bind(movie: Movie, movieAdapterListener: MovieAdapterListener) {
        this.movie = movie
        with(binding) {
            title.text = movie.title
            year.text = movie.year.toString()
        }

        binding.editButton.setOnClickListener {
            movieAdapterListener.onEdit(movie)
        }

        binding.deleteButton.setOnClickListener {
            movieAdapterListener.onDelete(movie)
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title && oldItem.year == newItem.year
    }
}