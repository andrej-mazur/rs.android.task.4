package com.example.watchlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchlist.databinding.FragmentMainBinding
import com.example.watchlist.db.Movie
import com.example.watchlist.ui.main.MovieAdapter
import com.example.watchlist.ui.main.MovieAdapterListener
import com.example.watchlist.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private val movieAdapter = MovieAdapter(object : MovieAdapterListener {

        override fun onEdit(movie: Movie) {
            navigateToEdit(movie)
        }

        override fun onDelete(movie: Movie) {
            delete(movie)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            movieList.layoutManager = LinearLayoutManager(context)
            movieList.adapter = movieAdapter

            fab.setOnClickListener {
                navigateToCreate()
            }
        }

        mainViewModel.sortedMovies.observe(viewLifecycleOwner, { movies ->
            movieAdapter.submitList(movies)
        })
    }

    fun navigateToEdit(movie: Movie) {
        val direction = MainFragmentDirections.actionMainToEdit(movieId = movie.id, movieTitle = movie.title, movieYear = movie.year)
        findNavController().navigate(direction)
    }

    fun navigateToCreate() {
        val direction = MainFragmentDirections.actionMainToEditEmpty()
        findNavController().navigate(direction)
    }

    fun delete(movie: Movie) {
        mainViewModel.deleteMovie(movie)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}