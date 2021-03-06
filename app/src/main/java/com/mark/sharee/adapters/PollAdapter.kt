package com.mark.sharee.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.model.poll.Question
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.question_boolean_item.*
import kotlinx.android.synthetic.main.question_boolean_item.questionLayout
import kotlinx.android.synthetic.main.question_boolean_item.radioGrp
import kotlinx.android.synthetic.main.question_generic_item.*
import kotlinx.android.synthetic.main.question_item.*
import kotlinx.android.synthetic.main.question_item.view.*
import kotlinx.android.synthetic.main.question_numerical_item.*
import kotlinx.android.synthetic.main.question_textual_item.*
import kotlinx.android.synthetic.main.section_header_item.*
import timber.log.Timber


class PollAdapter : ListAdapter<PollAbstractDisplayItem, PollItemViewHolder>(DIFF_CALLBACK) {

    var items = listOf<PollAbstractDisplayItem>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PollItemViewHolder.create(parent, viewType)

    override fun onBindViewHolder(holderAdapter: PollItemViewHolder, position: Int) {
        holderAdapter.bind(holderAdapter.containerView, getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.ordinal
    }

    override fun submitList(list: MutableList<PollAbstractDisplayItem>?) {
        list?.let {
            items = list
            super.submitList(list)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PollAbstractDisplayItem>() {

            override fun areItemsTheSame(
                oldItem: PollAbstractDisplayItem,
                newItem: PollAbstractDisplayItem
            ): Boolean {
                return oldItem.type == newItem.type
            }

            override fun areContentsTheSame(
                oldItem: PollAbstractDisplayItem,
                newItem: PollAbstractDisplayItem
            ) =
                when (oldItem.type) {
                    PollAbstractDisplayItem.PollItemType.HEADER -> (oldItem as PollHeaderItem).title == (newItem as PollHeaderItem).title
                    PollAbstractDisplayItem.PollItemType.BOOLEAN -> (oldItem as BooleanQuestionItem).question.id == (newItem as BooleanQuestionItem).question.id
                    PollAbstractDisplayItem.PollItemType.NUMERICAL -> (oldItem as NumericalQuestionItem).question.id == (newItem as NumericalQuestionItem).question.id
                    PollAbstractDisplayItem.PollItemType.TEXTUAL -> (oldItem as TextualQuestionItem).question.id == (newItem as TextualQuestionItem).question.id
                    PollAbstractDisplayItem.PollItemType.GENERIC -> (oldItem as GenericQuestionItem).question.id == (newItem as GenericQuestionItem).question.id
                }
        }
    }

}

class PollItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(view: View, pollItem: PollAbstractDisplayItem, position: Int) {

        questionText
        when (pollItem.type) {
            PollAbstractDisplayItem.PollItemType.HEADER -> bindHeaderItem(
                view,
                (pollItem as PollHeaderItem).title
            )
            PollAbstractDisplayItem.PollItemType.BOOLEAN -> bindBooleanQuestion(
                view,
                (pollItem as BooleanQuestionItem).question
            )
            PollAbstractDisplayItem.PollItemType.NUMERICAL -> bindNumericalQuestion(
                view,
                (pollItem as NumericalQuestionItem).question
            )
            PollAbstractDisplayItem.PollItemType.TEXTUAL -> bindTextualQuestion(
                view,
                (pollItem as TextualQuestionItem).question
            )
            PollAbstractDisplayItem.PollItemType.GENERIC -> bindGenericQuestion(
                view,
                (pollItem as GenericQuestionItem).question
            )
        }
    }

    private fun bindHeaderItem(view: View, title: String) {
        sectionText.text = title
    }

    private fun bindBooleanQuestion(view: View, question: Question) {
        questionLayout.questionText.text = question.question
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                yesBtn.id -> question.answer = true
                noBtn.id -> question.answer = false
            }

            Timber.d("BooleanQuestion [$question.id]=[${question.answer}]")
        }
    }

    private fun bindNumericalQuestion(view: View, question: Question) {
        questionLayout.questionText.text = question.question
        radioGrp.setOnCheckedChangeListener { _, checkedId ->
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
        questionLayout.questionText.text = question.question
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

    private fun bindGenericQuestion(view: View, question: Question) {
        questionLayout.questionText.text = question.question

        manipulateQuestionAnswers(
            question,
            view.resources.getString(R.string.poll_screen_generic_answer_hint)
        )

        var arrayAdapter =
            object : ArrayAdapter<String>(view.context, R.layout.spinner_item, question.answers) {
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView

                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(parent.resources.getColor(R.color.grey_20))
                    } else {
                        tv.setTextColor(parent.resources.getColor(R.color.black))
                    }

                    return view
                }
            }

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(genericAnswerSpinner) {
            adapter = arrayAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.i("onNothingSelected")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Timber.i("onItemSelected")
                    question.answer = position

                    if (position == 0) {
                        view?.resources?.let {
                            (parent?.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.grey_20))
                        }

                    }

                    question.answer = position - 1
                }
            }
        }
    }

    private fun manipulateQuestionAnswers(question: Question, hintText: String) {
        val manipulatedList = mutableListOf(hintText)
        manipulatedList.addAll(question.answers)
        question.answers = manipulatedList
    }

    companion object {

        fun create(parent: ViewGroup, viewType: Int): PollItemViewHolder {
            return when (viewType) {
                PollAbstractDisplayItem.PollItemType.BOOLEAN.ordinal ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.question_boolean_item,
                            parent,
                            false
                        )
                    )

                PollAbstractDisplayItem.PollItemType.NUMERICAL.ordinal ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.question_numerical_item,
                            parent,
                            false
                        )
                    )

                PollAbstractDisplayItem.PollItemType.TEXTUAL.ordinal ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.question_textual_item,
                            parent,
                            false
                        )
                    )

                PollAbstractDisplayItem.PollItemType.GENERIC.ordinal ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.question_generic_item,
                            parent,
                            false
                        )
                    )

                PollAbstractDisplayItem.PollItemType.HEADER.ordinal ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.section_header_item,
                            parent,
                            false
                        )
                    )

                else ->
                    PollItemViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.question_textual_item,
                            parent,
                            false
                        )
                    )
            }
        }
    }
}
