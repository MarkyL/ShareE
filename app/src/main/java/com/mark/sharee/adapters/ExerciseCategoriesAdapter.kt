package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.network.model.responses.ExerciseCategory
import com.mark.sharee.network.model.responses.GeneralPollResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.generic_title_item.*

class ExerciseCategoriesAdapter(listener: AdapterListener<ExerciseCategory>) : BaseAdapter<ExerciseCategory>(listener) {
    override fun getLayoutId(position: Int, obj: ExerciseCategory): Int {
        return R.layout.generic_title_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseCategoryViewHolder(view)
    }
}

class ExerciseCategoryViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<ExerciseCategory> {

    override fun bind(data: ExerciseCategory) {
        title.text = data.name
    }
}