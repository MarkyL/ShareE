package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.GeneralPollResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.generic_title_item.*

class PollsAdapter(listener: AdapterListener<GeneralPollResponse>) : BaseAdapter<GeneralPollResponse>(listener) {
    override fun getLayoutId(position: Int, obj: GeneralPollResponse): Int {
        return R.layout.generic_title_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return PollViewHolder(view)
    }
}

class PollViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<GeneralPollResponse> {

    override fun bind(data: GeneralPollResponse) {
        title.text = data.name
    }
}