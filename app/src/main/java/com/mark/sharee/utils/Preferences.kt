package com.mark.sharee.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sharee.R


class Preferences(private val context: Context) {

    var fontStyle: FontStyle
        get() = FontStyle.valueOf(open().getString(FONT_STYLE, FontStyle.Small.name)!!)
        set(style) { edit().putString(FONT_STYLE, style.name).commit() }

    protected fun open(): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    protected fun edit(): SharedPreferences.Editor {
        return open().edit()
    }

    companion object {
        private val FONT_STYLE = "FONT_STYLE"
    }

}

enum class FontStyle private constructor(val resId: Int, val title: String) {
    Small(R.style.FontStyle_Small, "רגיל"),
    Medium(R.style.FontStyle_Medium, "בינוני"),
    Large(R.style.FontStyle_Large, "גדול")
}