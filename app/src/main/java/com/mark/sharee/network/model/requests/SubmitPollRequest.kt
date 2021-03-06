package com.mark.sharee.network.model.requests

import com.mark.sharee.model.poll.AnsweredQuestion

data class SubmitPollRequest(
    val pollId: String,
    val answers: List<AnsweredQuestion>)
