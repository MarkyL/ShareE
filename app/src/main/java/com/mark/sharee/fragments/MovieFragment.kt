package com.mark.sharee.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharee.R
import com.mark.sharee.adapters.MovieAdapter
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.movie_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class MovieFragment : ShareeFragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }

    private val viewModel : MovieViewModel by sharedViewModel()
    private val movieAdapter: MovieAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 30, true))
            this.adapter = movieAdapter
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val dataState = it ?: return@Observer
            progressBar.visibility = if (dataState.showProgress) View.VISIBLE else View.GONE
            if (dataState.movies != null && !dataState.movies.consumed) {
                dataState.movies.consume()?.let { movies ->
                    movieAdapter.submitList(movies)
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { errorResource ->
                    Toast.makeText(context, resources.getString(errorResource), Toast.LENGTH_SHORT).show()
                    // handle error state
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatchInputEvent(FetchMovies)
    }

}
