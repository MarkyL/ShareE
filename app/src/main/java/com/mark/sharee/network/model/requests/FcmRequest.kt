package com.mark.sharee.network.model.requests

data class FcmRequest(
    val verificationToken: String,
    val fcmToken: String)
