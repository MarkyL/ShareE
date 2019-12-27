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
        private const val CURRENT_USER_FILE_NAME = "current_user"
        private const val TOKEN_PREFERENCE = "token"

        fun create(): User {
            me = User()
            return User()
        }

        fun register(context: Context) {
            preferences =
                context.getSharedPreferences(CURRENT_USER_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun me(): User? {
            if (me == null && preferences.getString(TOKEN_PREFERENCE, null) != null) {
                me = User()
                Timber.i("User object is null ! created new User")
            }
            return me
        }
    }

    @SuppressLint("ApplySharedPref")
    fun updateToken(token: String) {
        preferences.edit().putString(TOKEN_PREFERENCE, token).commit()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN_PREFERENCE, null)
    }

}