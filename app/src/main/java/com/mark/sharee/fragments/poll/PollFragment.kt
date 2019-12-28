package com.mark.sharee.fragments.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.PollAdapter
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PollFragment : ShareeFragment() {

    private val viewModel: PollViewModel by sharedViewModel()
    private val pollAdapter: PollAdapter = PollAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1,30, true))
            this.adapter = pollAdapter
        }

        registerViewModel()

        viewModel.dispatchInputEvent(GetPoll)
    }

    private fun registerViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            val dataState = it ?: return@Observer
            progressBar.visibility = if (dataState.showProgress) View.VISIBLE else View.GONE
            if (dataState.response != null && !dataState.response.consumed) {
                dataState.response.consume()?.let { response ->
                    Timber.i("registerViewModel response = $response")
                    pollName.text = response.name
                    pollAdapter.submitList(response.questions)
                }
            }
            if (dataState.error != null && !dataState.error.consumed) {
                dataState.error.consume()?.let { errorResource ->
                    Toast.makeText(context, resources.getString(errorResource), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}