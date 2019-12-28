package com.mark.sharee.network.model.requests

import com.mark.sharee.model.poll.AnsweredQuestion

data class SubmitPollRequest(
    override val verificationToken: String,
    val answeredQuestions: List<AnsweredQuestion>): BasicRequest(verificationToken)