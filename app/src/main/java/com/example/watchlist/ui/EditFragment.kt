package com.example.watchlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.watchlist.R
import com.example.watchlist.databinding.FragmentEditBinding
import com.example.watchlist.db.Movie
import com.example.watchlist.setToolbarTitle
import com.example.watchlist.viewmodel.MainViewModel

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val args: EditFragmentArgs by navArgs()

    private val edit get() = args.movieId > 0

    private val id get() = if (edit) args.movieId else 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        if (edit) {
            binding.title.setText(args.movieTitle)
            binding.year.setText(args.movieYear.toString())

            setToolbarTitle(R.string.edit_movie)
        } else {
            setToolbarTitle(R.string.add_movie)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.save.setOnClickListener {
            val title = binding.title.text.toString()
            if (title.isBlank()) {
                Toast.makeText(context, "Title must be specified", Toast.LENGTH_SHORT).show()
                binding.title.requestFocus()
                return@setOnClickListener
            }

            val yearValue = binding.year.text.toString()
            if (yearValue.isBlank()) {
                Toast.makeText(context, "Year must be specified", Toast.LENGTH_SHORT).show()
                binding.year.requestFocus()
                return@setOnClickListener
            }

            val year = yearValue.toIntOrNull() ?: -1
            if (year < 1900 || year > 2100) {
                Toast.makeText(context, "Year must be between ${1900} and ${2100}", Toast.LENGTH_SHORT).show()
                binding.year.requestFocus()
                return@setOnClickListener
            }

            val movie = Movie(id, title, year)
            if (edit) {
                viewModel.updateMovie(movie)
            } else {
                viewModel.insertMovie(movie)
            }

            val direction = EditFragmentDirections.actionEditToMain()
            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}