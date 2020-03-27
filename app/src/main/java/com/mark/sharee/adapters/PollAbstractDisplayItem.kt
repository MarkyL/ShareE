package com.mark.sharee.adapters

import com.mark.sharee.model.poll.Question

abstract class PollAbstractDisplayItem(open val type: PollItemType) {

    enum class PollItemType {
        HEADER,
        BOOLEAN,
        NUMERICAL,
        TEXTUAL,
        GENERIC
    }
}

data class PollHeaderItem(val title: String) : PollAbstractDisplayItem(PollItemType.HEADER)

data class BooleanQuestionItem(val question: Question) : PollAbstractDisplayItem(PollItemType.BOOLEAN)

data class NumericalQuestionItem(val question: Question) : PollAbstractDisplayItem(PollItemType.NUMERICAL)

data class TextualQuestionItem(val question: Question) : PollAbstractDisplayItem(PollItemType.TEXTUAL)

data class GenericQuestionItem(val question: Question) : PollAbstractDisplayItem(PollItemType.GENERIC)