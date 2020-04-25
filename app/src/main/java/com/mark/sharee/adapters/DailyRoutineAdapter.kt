package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.DailyActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.daily_activity_item.*


class DailyRoutineAdapter : BaseAdapter<DailyActivity>() {
    override fun getLayoutId(position: Int, obj: DailyActivity): Int {
        return R.layout.daily_activity_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return DailyRoutineItemViewHolder(view)
    }
}

class DailyRoutineItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<DailyActivity> {

    override fun bind(data: DailyActivity) {
        timeText.text = StringBuilder().append(data.startTime).append(" - ").append(data.endTime)
        activityText.text = data.activityDescription
    }
}