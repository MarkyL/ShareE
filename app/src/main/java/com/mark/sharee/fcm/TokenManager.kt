package com.mark.sharee.fcm

import com.google.firebase.iid.FirebaseInstanceId
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenManager @Inject internal constructor() {
    private val subject: BehaviorSubject<String> = BehaviorSubject.create()
    private val refreshTokenSubject: BehaviorSubject<String> = BehaviorSubject.create()

    init {
       FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { r -> subject.onNext(r.token) }
    }

    val observable: Observable<String>
        get() = subject.asObservable()

    val refreshTokenObservable: Observable<String>
        get() = refreshTokenSubject.asObservable()



    fun onTokenRefresh(token: String) {
        subject.onNext(token)
        refreshTokenSubject.onNext(token)
    }
}