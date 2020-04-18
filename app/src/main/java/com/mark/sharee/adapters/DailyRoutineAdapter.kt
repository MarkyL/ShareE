package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.DailyActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.daily_activity_item.*
import timber.log.Timber
import java.lang.StringBuilder


class DailyRoutineAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<DailyActivity>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_activity_item, parent, false)
        vh = DailyRoutineItemViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is DailyRoutineItemViewHolder) {
            Timber.e("DailyRoutineAdapter - wrong holder type"); return
        }

        val dailyActivity = items[position]

        holder.timeText.text = StringBuilder().append(dailyActivity.startTime).append(" - ").append(dailyActivity.endTime)
        holder.activityText.text = dailyActivity.activityDescription
    }

    fun updateItems(newItems: List<DailyActivity>) {
        Timber.i("updateItems: items update size ${newItems.size}")
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class DailyRoutineItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer