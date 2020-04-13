package com.mark.sharee.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.fragments.poll.PollSectionDiffCallback
import com.mark.sharee.model.poll.Question
import com.mark.sharee.network.model.responses.GeneralPollResponse
import com.mark.sharee.network.model.responses.PollSection
import com.mark.sharee.utils.GridSpacingItemDecoration
import com.mark.sharee.utils.Tools
import com.mark.sharee.utils.ViewAnimation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.poll_section_item.*
import timber.log.Timber


class PollSectionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<PollSection>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.poll_section_item, parent, false)
        vh = PollSectionItemViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is PollSectionItemViewHolder) {
            Timber.e("PollSectionsAdapter - wrong holder type"); return
        }

        val pollSection = items[position]
        holder.sectionName.text = pollSection.name

        holder.headerLyt.setOnClickListener { toggleSection(pollSection, holder) }
        holder.btnExpand.setOnClickListener { toggleSection(pollSection, holder) }

        if (pollSection.expanded) {
            holder.lyt_expand.visibility = View.VISIBLE
        } else {
            holder.lyt_expand.visibility = View.GONE
        }
        Tools.toggleArrow(pollSection.expanded, holder.btnExpand, false)

        val questionsAdapter = PollAdapter()
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = questionsAdapter
        }

        questionsAdapter.submitList(generatePollDisplayItems(pollSection))
    }

    private fun toggleSection(pollSection: PollSection, holder: PollSectionItemViewHolder) {
        pollSection.expanded =
            toggleLayoutExpand(!pollSection.expanded, holder.btnExpand, holder.lyt_expand)
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

    fun updateItems(newItems: List<PollSection>) {
        Timber.i("updateItems: items update size ${newItems.size}")
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(PollSectionDiffCallback(this.items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return items.size
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

class PollSectionItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer