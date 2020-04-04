package com.mark.sharee.utils

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

abstract class OnKeyboardActionListener(private val keyboardAction: KeyboardAction) :
        TextView.OnEditorActionListener {

    enum class KeyboardAction constructor(val imeAction: Int) {
        Next(EditorInfo.IME_ACTION_NEXT),
        Done(EditorInfo.IME_ACTION_DONE),
        Send(EditorInfo.IME_ACTION_SEND),
        Search(EditorInfo.IME_ACTION_SEARCH)
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean =
            actionId == keyboardAction.imeAction && onEditorAction()

    abstract fun onEditorAction(): Boolean
}