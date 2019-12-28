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

    var items = listOf<Question>()
        private set

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

            override fun areItemsTheSame(oldItem: Question, newItem: Question) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Question, newItem: Question) =
                oldItem.id == newItem.id &&
                        oldItem.question == newItem.question &&
                        oldItem.type == newItem.type
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
                yesBtn.id -> question.answer = true
                noBtn.id -> question.answer = false
            }

            Timber.d("BooleanQuestion [$question.id]=[${question.answer}]")
        }
    }

    private fun bindNumericalQuestion(view: View, question: Question) {
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                oneBtn.id -> question.answer = 1
                twoBtn.id -> question.answer = 2
                threeBtn.id -> question.answer = 3
                fourBtn.id -> question.answer = 4
                fiveBtn.id -> question.answer = 5
            }
            Timber.d("NumericalQuestion [$question.id]=[${question.answer}")
        }
    }

    private fun bindTextualQuestion(view: View, question: Question) {
        textAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                question.answer = s.toString()
                Timber.d("TextualQuestion [$question.id]=[${question.answer}")
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
