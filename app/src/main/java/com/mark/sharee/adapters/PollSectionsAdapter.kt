package com.mark.sharee.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.fragments.poll.PollSectionDiffCallback
import com.mark.sharee.model.poll.Question
import com.mark.sharee.network.model.responses.PollSection
import com.mark.sharee.utils.GridSpacingItemDecoration
import com.mark.sharee.utils.Tools
import com.mark.sharee.utils.ViewAnimation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.poll_section_item.*


class PollSectionsAdapter : BaseAdapter<PollSection>() {

    init {
        setAnimationType(0)
    }

    override fun getLayoutId(position: Int, obj: PollSection): Int {
        return R.layout.poll_section_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return PollSectionItemViewHolder(view)
    }

    override fun submitList(listItems: List<PollSection>) {
        val diffResult =
            DiffUtil.calculateDiff(PollSectionDiffCallback(this.items, listItems))
        this.items = listItems
        diffResult.dispatchUpdatesTo(this)
    }
}

class PollSectionItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<PollSection> {

    override fun bind(data: PollSection) {
        sectionName.text = data.name

        headerLyt.setOnClickListener { toggleSection(data, this) }
        btnExpand.setOnClickListener { toggleSection(data, this) }

        if (data.expanded) {
            lyt_expand.visibility = View.VISIBLE
        } else {
            lyt_expand.visibility = View.GONE
        }
        Tools.toggleArrow(data.expanded, btnExpand, false)

        val questionsAdapter = PollAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = questionsAdapter
        }

        questionsAdapter.submitList(generatePollDisplayItems(data))
    }

    private fun generatePollDisplayItems(pollSection: PollSection): MutableList<PollAbstractDisplayItem>  {
        val pollDisplayItems = mutableListOf<PollAbstractDisplayItem>()
        pollSection.questions.forEach { question ->
            when (question.type) {
                Question.QuestionType.BOOLEAN -> pollDisplayItems.add(BooleanQuestionItem(question))
                Question.QuestionType.NUMERICAL -> pollDisplayItems.add(NumericalQuestionItem(question))
                Question.QuestionType.TEXTUAL -> pollDisplayItems.add(TextualQuestionItem(question))
                Question.QuestionType.GENERIC -> pollDisplayItems.add(GenericQuestionItem(question))
            }
        }
        return pollDisplayItems
    }

    private fun toggleSection(pollSection: PollSection, holder: PollSectionItemViewHolder) {
        pollSection.expanded =
            toggleLayoutExpand(!pollSection.expanded, holder.btnExpand, holder.lyt_expand)
    }

    private fun toggleLayoutExpand(show: Boolean, view: View, lyt_expand: View): Boolean {
        Tools.toggleArrow(show, view)
        if (show) {
            ViewAnimation.expand(lyt_expand)
        } else {
            ViewAnimation.collapse(lyt_expand)
        }
        return show
    }

}