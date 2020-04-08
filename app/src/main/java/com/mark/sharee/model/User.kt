package com.mark.sharee.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.mark.sharee.utils.StringUtils
import timber.log.Timber

data class User(val verificationToken: String,
                val phoneNumber: String,
                val patientName: String) {

    constructor() : this(StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING)



    companion object {

        private var me: User? = null
        private lateinit var preferences: SharedPreferences
        const val CURRENT_USER_FILE_NAME = "current_user"
        private const val VERIFICATION_TOKEN_PREFERENCE = "verification_token"
        private const val PHONE_PREFERENCE = "phone"
        private const val FIRE_BASE_AUTH_TOKEN = "fb_token"

        fun create(phoneNumber: String, fireBaseAuthToken: String): User {
            val editor = preferences.edit()
            editor.putString(PHONE_PREFERENCE, phoneNumber)
            editor.putString(FIRE_BASE_AUTH_TOKEN, fireBaseAuthToken)
            editor.apply()

            me = User()
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

//        fun isVerificationTokenAvailable(): Boolean {
//            me.getToken().let {
//                return it != null
//            }
//        }
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

}