package com.mark.sharee.widgets

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat

import com.example.sharee.R
import com.mark.sharee.utils.FontManager

class ShareeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0) : AppCompatTextView(context, attrs, defStyle) {

    init {
        setTextAppearance(R.style.ShareeTextViewStyle)
//        if (!isInEditMode) {
//            FontManager.forAttrs(R.styleable.ShareeTextView, R.styleable.ShareeTextView_sharee_font)
//                .apply(this, attrs, defStyle)
//        }
    }

    fun setAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
    }

    fun disabeAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE
        )
    }
}
