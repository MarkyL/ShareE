package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.Exercise
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.exercise_item.*

class ExercisesAdapter(listener: ExercisesAdapterListener) : BaseAdapter<Exercise>(listener) {

    init {
        setAnimationType(0)
    }

    override fun getLayoutId(position: Int, obj: Exercise): Int {
        return R.layout.exercise_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(view, listener as ExercisesAdapterListener)
    }

}

class ExerciseViewHolder constructor(override val containerView: View, val listener: ExercisesAdapterListener) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<Exercise> {

    override fun bind(data: Exercise) {
        checkedTextView.text = data.description

        data.url?.let { link ->
            linkImage.visibility = View.VISIBLE
            linkImage.setOnClickListener { listener.onOpenUrlClick(link) }
        }
    }

}

interface ExercisesAdapterListener : BaseAdapter.AdapterListener<Exercise> {
    fun onOpenUrlClick(url: String)
}