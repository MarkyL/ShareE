package com.mark.sharee.model

import android.content.Context
import android.content.SharedPreferences
import com.mark.sharee.utils.StringUtils
import timber.log.Timber

data class User(var verificationToken: String, val phoneNumber: String) {

    constructor() : this(StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING)

    companion object {

        private var me: User? = null
        private lateinit var preferences: SharedPreferences
        const val CURRENT_USER_FILE_NAME = "current_user"
        private const val VERIFICATION_TOKEN_PREFERENCE = "verification_token"
        private const val PHONE_PREFERENCE = "phone"
        private const val FIRE_BASE_AUTH_TOKEN = "fb_token"
        private const val FCM_TOKEN = "fcm_token"
        private const val FONT_STYLE = "font_style"

        fun create(verificationToken: String, phoneNumber: String, fireBaseAuthToken: String): User {
            me = User(verificationToken, phoneNumber)

            me?.let {
                with (it) {
                    setPhonePreference(phoneNumber)
                    updateToken(verificationToken)
                    setFireBaseAuthToken(fireBaseAuthToken)
                }
            }

            return User()
        }

        fun register(context: Context) {
            preferences =
                context.getSharedPreferences(CURRENT_USER_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun me(): User? {
            if (me == null && preferences.getString(VERIFICATION_TOKEN_PREFERENCE, null) != null) {
                me = User()
                Timber.i("User object is null ! created new User")
            }
            return me
        }

    }

    fun updateToken(token: String) {
        preferences.edit().putString(VERIFICATION_TOKEN_PREFERENCE, token).apply()
    }

    fun getToken(): String {
        return preferences.getString(VERIFICATION_TOKEN_PREFERENCE, StringUtils.EMPTY_STRING).toString()
    }

    fun setPhonePreference(phoneNumber: String) {
        preferences.edit().putString(PHONE_PREFERENCE, phoneNumber).apply()
    }

    fun getPhone(): String {
        return preferences.getString(PHONE_PREFERENCE, StringUtils.EMPTY_STRING).toString()
    }

    fun setFireBaseAuthToken(fireBaseAuthToken: String) {
        preferences.edit().putString(FIRE_BASE_AUTH_TOKEN, fireBaseAuthToken).apply()
    }

    fun getFireBaseAuthToken(): String {
        return preferences.getString(FIRE_BASE_AUTH_TOKEN, StringUtils.EMPTY_STRING).toString()
    }

    fun setFcmToken(fcmToken: String) {
        preferences.edit().putString(FCM_TOKEN, fcmToken).apply()
    }

    fun getFcmToken(): String {
        return preferences.getString(FCM_TOKEN, StringUtils.EMPTY_STRING).toString()
    }

}