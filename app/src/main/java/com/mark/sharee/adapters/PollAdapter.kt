package com.mark.sharee.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.model.poll.Question
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_boolean_item.*
import kotlinx.android.synthetic.main.question_boolean_item.questionLayout
import kotlinx.android.synthetic.main.question_boolean_item.radioGrp
import kotlinx.android.synthetic.main.question_item.view.*
import kotlinx.android.synthetic.main.question_numerical_item.*
import kotlinx.android.synthetic.main.question_textual_item.*
import timber.log.Timber

class PollAdapter : ListAdapter<Question, QuestionViewHolder>(DIFF_CALLBACK) {

    private var items = listOf<Question>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuestionViewHolder.create(parent, viewType)

    override fun onBindViewHolder(holderAdapter: QuestionViewHolder, position: Int) {
        holderAdapter.bind(holderAdapter.containerView, getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        val question = getItem(position)
        return when (question.type) {
            Question.QuestionType.BOOLEAN -> Question.QuestionType.BOOLEAN.ordinal
            Question.QuestionType.NUMERICAL -> Question.QuestionType.NUMERICAL.ordinal
            Question.QuestionType.TEXTUAL -> Question.QuestionType.TEXTUAL.ordinal
        }
    }

    override fun submitList(list: MutableList<Question>?) {
        list?.let {
            items = list
            super.submitList(list)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Question>() {

            override fun areItemsTheSame(
                oldItem: Question,
                newItem: Question
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Question,
                newItem: Question
            ) =
                oldItem == newItem
        }
    }

}

class QuestionViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(view: View, question: Question, position: Int) {
        questionLayout.questionNumber.text =
            (position + 1).toString() + ")"// +1 in order to show human number and not index.
        questionLayout.questionText.text = question.question
        when (question.type) {
            Question.QuestionType.BOOLEAN -> bindBooleanQuestion(view, question)
            Question.QuestionType.NUMERICAL -> bindNumericalQuestion(view, question)
            Question.QuestionType.TEXTUAL -> bindTextualQuestion(view, question)
        }
    }

    private fun bindBooleanQuestion(view: View, question: Question) {
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                yesBtn.id -> Timber.i("Boolean question - YES")
                noBtn.id -> Timber.i("Boolean question - NO")
            }
        }
    }

    private fun bindNumericalQuestion(view: View, question: Question) {
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                oneBtn.id -> Timber.i("Numerical question - 1")
                twoBtn.id -> Timber.i("Numerical question - 2")
                threeBtn.id -> Timber.i("Numerical question - 3")
                fourBtn.id -> Timber.i("Numerical question - 4")
                fiveBtn.id -> Timber.i("Numerical question - 5")
            }
        }
    }

    private fun bindTextualQuestion(view: View, question: Question) {
        textAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Timber.i("TextAnswer afterTextChanged() - ${s.toString()}")
            }

        })

    }

    companion object {

        fun create(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            return when (viewType) {
                Question.QuestionType.BOOLEAN.ordinal ->
                    QuestionViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.question_boolean_item, parent, false)
                    )
                Question.QuestionType.NUMERICAL.ordinal ->
                    QuestionViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.question_numerical_item, parent, false)
                    )
                Question.QuestionType.TEXTUAL.ordinal ->
                    QuestionViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.question_textual_item, parent, false)
                    )
                else -> QuestionViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.question_boolean_item, parent, false)
                )
            }
        }
    }
}
