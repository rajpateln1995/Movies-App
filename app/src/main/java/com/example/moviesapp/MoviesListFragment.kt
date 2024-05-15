package com.example.moviesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.databinding.FragmentLatestMoviesBinding

class MoviesListFragment(private val type: String) : Fragment(), MoviesListener {

    private val binding: FragmentLatestMoviesBinding by lazy {
        FragmentLatestMoviesBinding.inflate(layoutInflater)
    }

    private val viewModel: MoviesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        initUi()
        initData()
        return binding.root
    }

    private fun initData() {
        viewModel.getMovies(type, requireContext())
    }

    private fun initUi() {

        if (type == Constants.LATEST) {
            val adapter = MoviesAdapter(requireContext(), this)
            binding.rvMoviesList.adapter = adapter
            binding.rvMoviesList.layoutManager = LinearLayoutManager(requireContext())
            viewModel.latestMoviesList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        if (type == Constants.POPULAR) {
            val adapter = MoviesAdapter(requireContext(), this)
            binding.rvMoviesList.adapter = adapter
            binding.rvMoviesList.layoutManager = LinearLayoutManager(requireContext())
            viewModel.popularMoviesList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        viewModel.showLoadingView.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvLoading.visibility = View.VISIBLE
            } else {
                binding.tvLoading.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val TAG = "LatestMoviesFragment"
    }

    override fun loadMoreData() {
        viewModel.getMovies(Constants.LATEST, requireContext())
    }
}

interface MoviesListener {
    fun loadMoreData()
}