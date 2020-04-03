package com.mark.sharee.fragments.poll

import androidx.recyclerview.widget.DiffUtil
import com.mark.sharee.network.model.responses.PollSection
import timber.log.Timber.i

class PollSectionDiffCallback(private var oldList : List<PollSection>, private var newList : List<PollSection>) : DiffUtil.Callback() {

    init {
        i("PollSectionDiffCallback: created...")
    }
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.name == newItem.name && newItem.questions.size == oldItem.questions.size
    }
}