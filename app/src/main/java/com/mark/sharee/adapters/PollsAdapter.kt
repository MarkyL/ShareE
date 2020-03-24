package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.GeneralPollResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.poll_item.*

class PollsAdapter(private var listener: PollsAdapterListener) : RecyclerView.Adapter<PollViewHolder>() {

    var items = listOf<GeneralPollResponse>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PollViewHolder.create(parent)

    override fun onBindViewHolder(holderAdapter: PollViewHolder, position: Int) {
        holderAdapter.bind(holderAdapter.containerView, items[position], listener)
    }

    override fun getItemViewType(position: Int): Int {
        return POLL_TYPE
    }

    fun submitList(list: MutableList<GeneralPollResponse>?) {
        list?.let {
            items = it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        const val POLL_TYPE = 1
    }

}

class PollViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(
        view: View,
        poll: GeneralPollResponse,
        listener: PollsAdapterListener
    ) {
        pollName.text = poll.name

        view.setOnClickListener { listener.onPollClick(poll)}
    }

    companion object {

        fun create(parent: ViewGroup): PollViewHolder {
            return PollViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.poll_item, parent, false)
            )
        }
    }
}

interface PollsAdapterListener {
    fun onPollClick(poll: GeneralPollResponse)
}
